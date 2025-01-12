package com.multiplatform.time_management_backend.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.multiplatform.time_management_backend.exeption.AuthenticationInvalidTokenException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import com.multiplatform.time_management_backend.exeption.UnauthenticatedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Override
    public String validateTokenAndGetUsername(String token, TokenService tokenService) {
        try {
            return tokenService.validateTokenAndGetUsername(token);
        } catch (TokenValidationException e) {
            throw new AuthenticationInvalidTokenException(e);
        }
    }

    @Override
    public String generateToken(UserDetails userDetails, TokenService tokenService) {
        return tokenService.buildToken(userDetails);
    }

    @Override
    public Cookie createTokenCookie(String token, boolean isSecure, TokenService tokenService) {
        return tokenService.buildTokenCookie(token, isSecure);
    }
    @Override
    public String extractAccessTokenFromRequest(HttpServletRequest request) {
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

    public record AlgorithmWithId(String id, Algorithm algorithm) {
        AlgorithmWithId(RSAKeyPairConfigurations.KeyPairWithId rsaKeyPairWithId) {
            this(rsaKeyPairWithId.keyId(),
                    Algorithm.RSA256((RSAPublicKey) rsaKeyPairWithId.keyPair().getPublic(),
                            (RSAPrivateKey) rsaKeyPairWithId.keyPair().getPrivate()));
        }
    }
}