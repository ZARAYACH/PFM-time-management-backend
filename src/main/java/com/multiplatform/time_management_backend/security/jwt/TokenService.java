package com.multiplatform.time_management_backend.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;


interface TokenService {
    DecodedJWT validateToken(String token) throws TokenValidationException;

    String buildToken(UserDetails userDetails, String sessionId) throws BadArgumentException;

    int getExpirationTimeInSeconds();

    Cookie buildTokenCookie(String token, boolean isSecure);

    String extractToken(HttpServletRequest request);

}
