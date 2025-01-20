package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.configuration.CustomRedisCacheConfigurations;
import com.multiplatform.time_management_backend.exeption.*;
import com.multiplatform.time_management_backend.security.service.SessionService;
import com.multiplatform.time_management_backend.user.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.multiplatform.time_management_backend.security.jwt.JwtService.*;
import static java.util.Arrays.stream;

@Service
@Slf4j
class RefreshTokenService implements TokenService {

    private final JWTVerifier verifier;
    private final AlgorithmWithId algorithmWithId;
    private final CacheManager cacheManager;
    private final Cache sessionCache;
    private final Cache refreshTokensCache;
    private final JwtConfigurationProperties jwtConfigurationProperties;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    public RefreshTokenService(RSAKeyPairConfigurations rsaKeyPairConfigurations, CacheManager cacheManager, JwtConfigurationProperties jwtConfigurationProperties, SessionRepository sessionRepository, SessionService sessionService) throws NoSuchAlgorithmException {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.algorithmWithId = AlgorithmWithId.fromKeyPair(rsaKeyPairConfigurations.generateRSAKeyPair());
        this.cacheManager = cacheManager;
        this.sessionCache = cacheManager.getCache(CustomRedisCacheConfigurations.SESSION_IDS_CACHE_NAME);
        this.refreshTokensCache = cacheManager.getCache(CustomRedisCacheConfigurations.REFRESH_JTI_CACHE_NAME);
        this.verifier = JWT.require(algorithmWithId.algorithm()).build();
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
    }

    @Override
    public DecodedJWT validateToken(String token) throws TokenValidationException {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            validateTokenClaims(decodedJWT);
            return decodedJWT;
        } catch (Exception e) {
            throw new TokenValidationException(e);
        }

    }

    private void validateTokenClaims(DecodedJWT decodedJWT) throws BadArgumentException {
        try {
            Assert.hasText(decodedJWT.getId(), "JTI can't be empty");
            Assert.hasText(decodedJWT.getSubject(), "Subject cannot be empty");
            Assert.isTrue(Objects.equals(decodedJWT.getClaim(TOKEN_TYPE_CLAIM_NAME).asString(), TOKEN_TYPE_REFRESH_NAME), "Invalid Token type ");
            Assert.hasText(decodedJWT.getClaim(SESSION_ID_CLAIM_NAME).asString(), "sessionId cannot be empty");
            Assert.isTrue(validJTI(decodedJWT.getId()), "JTI can't be empty");
            Assert.isTrue(validSession(decodedJWT.getClaim(SESSION_ID_CLAIM_NAME).asString()), "Session expired");
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }
    }

    private boolean validJTI(String id) {
        if (refreshTokensCache != null) {
            return refreshTokensCache.get(id) == null;
        }
        return true;
    }

    private boolean validSession(String sessionId) {
        if (sessionCache != null) {
            return sessionCache.get(sessionId) == null;
        }
        try {
            return sessionService.findById(sessionId).getExpiredAt().isAfter(LocalDateTime.now());
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public String buildToken(UserDetails userDetails, String sessionId) throws AuthenticationServiceUnavailableException, BadArgumentException {
        try {
            Assert.notNull(userDetails, "UserDetails must not be null");
            Assert.hasText(sessionId, "SessionId must not be null or empty");
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }
        Instant now = Instant.now();
        try {
            return JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withKeyId(algorithmWithId.id())
                    .withSubject(userDetails.getUsername())
                    .withClaim(ROLES_CLAIM_NAME, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withClaim(TOKEN_TYPE_CLAIM_NAME, TOKEN_TYPE_REFRESH_NAME)
                    .withClaim(SESSION_ID_CLAIM_NAME, sessionId)
                    .withIssuedAt(now)
                    .withExpiresAt(now.plusSeconds(jwtConfigurationProperties.getRefreshTokenValidityInSeconds()))
                    .sign(algorithmWithId.algorithm());
        } catch (Exception e) {
            throw new AuthenticationServiceUnavailableException("Auth service unavailable", e);
        }
    }

    @Override
    public Long getExpirationTimeInSeconds() {
        return jwtConfigurationProperties.getRefreshTokenValidityInSeconds();
    }

    @Override
    public Cookie buildTokenCookie(String token, boolean isSecure, String domain) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, token);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(isSecure);
        refreshTokenCookie.setDomain(domain);
        refreshTokenCookie.setPath("/api/v1/tokens");
        refreshTokenCookie.setMaxAge(Math.toIntExact(getExpirationTimeInSeconds()));
        return refreshTokenCookie;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        Cookie refreshTokenCookie = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            refreshTokenCookie = stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                    .findFirst().orElse(null);
        }
        if (refreshTokenCookie != null && StringUtils.isNotBlank(refreshTokenCookie.getValue())) {
            return refreshTokenCookie.getValue();
        }
        throw new UnauthenticatedException("Could not extract refresh token from request");
    }

    @Override
    public void blackListToken(String jti) {
        if (refreshTokensCache != null) {
            refreshTokensCache.put(jti, LocalDateTime.now().toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
        }
    }
}

