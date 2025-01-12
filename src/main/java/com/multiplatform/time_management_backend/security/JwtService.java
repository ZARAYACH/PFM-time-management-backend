package com.multiplatform.time_management_backend.security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtService {
    String validateTokenAndGetUsername(String token, TokenService tokenService);
    String generateToken(UserDetails userDetails, TokenService tokenService);
    Cookie createTokenCookie(String token, boolean isSecure, TokenService tokenService) ;
    String extractAccessTokenFromRequest(HttpServletRequest request);
}
