package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.AuthenticationInvalidTokenException;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JWTParser jwtParser = new JWTParser();

    @Override
    public DecodedJWT validateAccessToken(String token) {
        try {
            DecodedJWT decodedJWT = refreshTokenService.validateToken(token);
            validateSessionIdClaim(decodedJWT.getClaim("sessionId").asString());
            return decodedJWT;
        } catch (Exception e) {
            throw new AuthenticationInvalidTokenException("Invalid access token", e);
        }
    }

    @Override
    public DecodedJWT validateRefreshToken(String token) {
        try {
            DecodedJWT decodedJWT = refreshTokenService.validateToken(token);
            validateSessionIdClaim(decodedJWT.getClaim("sessionId").asString());
            return decodedJWT;
        } catch (Exception e) {
            throw new AuthenticationInvalidTokenException("Invalid refresh token", e);
        }
    }

    private void validateSessionIdClaim(String sessionId) throws BadArgumentException {
        try {
            Assert.hasText(sessionId, "sessionId cannot be empty");
            //TODO : add validation to see if session was blacklisted
        } catch (Exception e) {
            throw new BadArgumentException("Invalid sessionId", e);
        }
    }

    @Override
    public String generateAccessToken(UserDetails userDetails, String sessionId) throws BadArgumentException {
        return accessTokenService.buildToken(userDetails, sessionId);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails, String sessionId) throws BadArgumentException {
        return refreshTokenService.buildToken(userDetails, sessionId);
    }


    @Override
    public Cookie createAccessTokenCookie(String token, boolean isSecure) {
        return accessTokenService.buildTokenCookie(token, isSecure);
    }

    @Override
    public Cookie createRefreshTokenCookie(String token, boolean isSecure) {
        return refreshTokenService.buildTokenCookie(token, isSecure);
    }

    @Override
    public String extractAccessToken(HttpServletRequest request) {
        return accessTokenService.extractToken(request);
    }

    @Override
    public String extractRefreshToken(HttpServletRequest request) {
        return refreshTokenService.extractToken(request);
    }


}