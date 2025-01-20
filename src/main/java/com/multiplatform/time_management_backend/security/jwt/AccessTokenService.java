package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.AuthenticationServiceUnavailableException;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import com.multiplatform.time_management_backend.exeption.UnauthenticatedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.multiplatform.time_management_backend.security.jwt.JwtService.*;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
class AccessTokenService implements TokenService {

    private final JWTVerifier verifier;
    private final AlgorithmWithId algorithmWithId;
    private final JwtConfigurationProperties jwtConfigurationProperties;
    private final RedisCacheManager redisCacheManager;
    private final Cache sessionsCache;

    public AccessTokenService(RSAKeyPairConfigurations rsaKeyPairConfigurations, JwtConfigurationProperties jwtConfigurationProperties,
                              RedisCacheManager redisCacheManager) throws NoSuchAlgorithmException {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.algorithmWithId = AlgorithmWithId.fromKeyPair(rsaKeyPairConfigurations.generateRSAKeyPair());
        this.redisCacheManager = redisCacheManager;
        this.verifier = JWT.require(algorithmWithId.algorithm()).build();
        sessionsCache = redisCacheManager.getCache("sessions");
    }

    @Override
    public DecodedJWT validateToken(String token) throws TokenValidationException {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            validateTokenClaims(decodedJWT);
            return decodedJWT;
        } catch (Exception verificationEx) {
            throw new TokenValidationException(verificationEx);
        }
    }

    private void validateTokenClaims(DecodedJWT decodedJWT) throws BadArgumentException {
        try {
            Assert.hasText(decodedJWT.getSubject(), "Subject cannot be empty");
            Assert.isTrue(Objects.equals(decodedJWT.getClaim(TOKEN_TYPE_CLAIM_NAME).asString(), TOKEN_TYPE_ACCESS_NAME), "Invalid Token type ");
            Assert.hasText(decodedJWT.getClaim(SESSION_ID_CLAIM_NAME).asString(), "sessionId cannot be empty");
        } catch (Exception e) {
            throw new BadArgumentException(e);
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
                    .withClaim(SESSION_ID_CLAIM_NAME, sessionId)
                    .withClaim(TOKEN_TYPE_CLAIM_NAME, TOKEN_TYPE_ACCESS_NAME)
                    .withIssuedAt(now)
                    .withExpiresAt(now.plusSeconds(jwtConfigurationProperties.getAccessTokenValidityInSeconds()))
                    .sign(algorithmWithId.algorithm());
        } catch (Exception e) {
            throw new AuthenticationServiceUnavailableException("Auth service unavailable", e);
        }
    }

    @Override
    public Long getExpirationTimeInSeconds() {
        return jwtConfigurationProperties.getAccessTokenValidityInSeconds();
    }

    @Override
    public Cookie buildTokenCookie(String token, boolean isSecure, String domain) {
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(isSecure);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setDomain(domain);
        accessTokenCookie.setMaxAge(Math.toIntExact(getExpirationTimeInSeconds()));
        return accessTokenCookie;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Cookie authorizationCookie = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            authorizationCookie = stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_COOKIE_NAME))
                    .findFirst().orElse(null);
        }
        if (authorizationCookie != null && StringUtils.isNotBlank(authorizationCookie.getValue())) {
            return authorizationCookie.getValue();
        } else if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        throw new UnauthenticatedException("Could not extract access token from request");
    }

    @Override
    public void blackListToken(String jti) {
        //TODO: to do later
    }
}

