package com.multiplatform.time_management_backend.security;

import com.multiplatform.time_management_backend.exeption.TokenValidationException;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;


public interface TokenService {
    String validateTokenAndGetUsername(String token) throws TokenValidationException;

    String buildToken(UserDetails userDetails);

    int getExpirationTimeInSeconds();
    Cookie buildTokenCookie(String token, boolean isSecure);

}
