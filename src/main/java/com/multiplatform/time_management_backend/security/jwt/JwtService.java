package com.multiplatform.time_management_backend.security.jwt;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    DecodedJWT validateAccessToken(String token);

    DecodedJWT validateRefreshToken(String token);

    String generateAccessToken(UserDetails userDetails, String sessionId) throws BadArgumentException;

    String generateRefreshToken(UserDetails userDetails, String sessionId) throws BadArgumentException;

    //in production is secure should always be true
    Cookie createAccessTokenCookie(String token, boolean isSecure);

    Cookie createRefreshTokenCookie(String token, boolean isSecure);

    String extractAccessToken(HttpServletRequest request);

    String extractRefreshToken(HttpServletRequest request);

}
