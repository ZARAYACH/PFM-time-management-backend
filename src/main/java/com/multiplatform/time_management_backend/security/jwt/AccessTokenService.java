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
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
class AccessTokenService implements TokenService {

    private final RSAKeyPairConfigurations.KeyPairWithId accessTokenKeyPair;
    private final JWTVerifier verifier;
    private final AlgorithmWithId algorithmWithId;
    private final int jwtExpiration;

    public AccessTokenService(@Value("${security.jwt.token.access.expiration-time}") int jwtExpiration,
                              @Qualifier("AccessTokenKeyPair") RSAKeyPairConfigurations.KeyPairWithId accessTokenKeyPair) {
        this.jwtExpiration = jwtExpiration;
        this.algorithmWithId = AlgorithmWithId.fromKeyPair(accessTokenKeyPair);
        this.accessTokenKeyPair = accessTokenKeyPair;
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
                    .withJWTId(UUID.randomUUID().toString())
                    .withKeyId(accessTokenKeyPair.keyId())
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
        Cookie accessTokenCookie = new Cookie("access_token", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(isSecure);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(getExpirationTimeInSeconds()));
        return accessTokenCookie;
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Cookie authorizationCookie = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            authorizationCookie = stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("access_token"))
                    .findFirst().orElse(null);
        }
        if (authorizationCookie != null && StringUtils.isNotBlank(authorizationCookie.getValue())) {
            return authorizationCookie.getValue();
        } else if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        throw new UnauthenticatedException("Could not extract access token from request");
    }

}

