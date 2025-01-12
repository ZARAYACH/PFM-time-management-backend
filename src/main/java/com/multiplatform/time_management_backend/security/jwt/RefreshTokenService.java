package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.AuthenticationServiceUnavailableException;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import com.multiplatform.time_management_backend.exeption.UnauthenticatedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@Slf4j
class RefreshTokenService implements TokenService {

    private final RSAKeyPairConfigurations.KeyPairWithId refreshTokenKeyPair;
    private final JWTVerifier verifier;
    private final AlgorithmWithId algorithmWithId;
    private final int jwtExpiration;

    public RefreshTokenService(@Value("${security.jwt.token.refresh.expiration-time}") int jwtExpiration,
                               @Qualifier("RefreshTokenKeyPair") RSAKeyPairConfigurations.KeyPairWithId refreshTokenKeyPair) {
        this.jwtExpiration = jwtExpiration;
        this.algorithmWithId = AlgorithmWithId.fromKeyPair(refreshTokenKeyPair);
        this.refreshTokenKeyPair = refreshTokenKeyPair;
        this.verifier = JWT.require(algorithmWithId.algorithm()).build();

    }

    @Override
    public DecodedJWT validateToken(String token) throws TokenValidationException {
        try {
            return verifier.verify(token);
        } catch (final JWTVerificationException verificationEx) {
            throw new TokenValidationException(verificationEx);
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
                    .withKeyId(refreshTokenKeyPair.keyId())
                    .withSubject(userDetails.getUsername())
                    .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withIssuedAt(now)
                    .withExpiresAt(now.plusSeconds(jwtExpiration))
                    .sign(algorithmWithId.algorithm());
        } catch (Exception e) {
            throw new AuthenticationServiceUnavailableException("Auth service unavailable", e);
        }
    }

    @Override
    public int getExpirationTimeInSeconds() {
        return jwtExpiration;
    }

    @Override
    public Cookie buildTokenCookie(String token, boolean isSecure) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", token);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(isSecure);
        refreshTokenCookie.setPath("/api/v1/token");
        refreshTokenCookie.setMaxAge(Math.toIntExact(getExpirationTimeInSeconds()));
        return refreshTokenCookie;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        Cookie refreshTokenCookie = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            refreshTokenCookie = stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("refresh_token"))
                    .findFirst().orElse(null);
        }
        if (refreshTokenCookie != null && StringUtils.isNotBlank(refreshTokenCookie.getValue())) {
            return refreshTokenCookie.getValue();
        }
        throw new UnauthenticatedException("Could not extract refresh token from request");
    }
}

