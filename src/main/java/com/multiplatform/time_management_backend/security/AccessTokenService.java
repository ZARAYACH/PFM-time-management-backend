package com.multiplatform.time_management_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.multiplatform.time_management_backend.exeption.AuthenticationServiceUnavailableException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccessTokenService implements TokenService {

    private final RSAKeyPairConfigurations.KeyPairWithId accessTokenKeyPair;
    private final JWTVerifier verifier;
    private final JwtServiceImpl.AlgorithmWithId algorithmWithId;
    private final int jwtExpiration;

    public AccessTokenService(@Value("${security.jwt.token.access.expiration-time}") int jwtExpiration,
                              @Qualifier("AccessTokenKeyPair") RSAKeyPairConfigurations.KeyPairWithId accessTokenKeyPair) {
        this.jwtExpiration = jwtExpiration;
        this.algorithmWithId = new JwtServiceImpl.AlgorithmWithId(accessTokenKeyPair);
        this.accessTokenKeyPair = accessTokenKeyPair;
        this.verifier = JWT.require(algorithmWithId.algorithm()).build();

    }

    @Override
    public String validateTokenAndGetUsername(String token) throws TokenValidationException {
        try {
            return verifier.verify(token).getSubject();
        } catch (final JWTVerificationException verificationEx) {
            throw new TokenValidationException(verificationEx);
        }

    }

    @Override
    public String buildToken(UserDetails userDetails) throws AuthenticationServiceUnavailableException {
        Instant now = Instant.now();
        try {
            return JWT.create()
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
}

