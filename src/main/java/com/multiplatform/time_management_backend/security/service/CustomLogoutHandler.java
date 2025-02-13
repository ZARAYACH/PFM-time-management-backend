package com.multiplatform.time_management_backend.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.security.jwt.JwtService;
import com.multiplatform.time_management_backend.user.model.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;
    private final SessionService sessionService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            DecodedJWT decodedJWT = jwtService.validateAccessToken(jwtService.extractAccessToken(request));
            Session session = sessionService.findById(decodedJWT.getClaim(JwtService.SESSION_ID_CLAIM_NAME).asString());
            sessionService.terminate(session);

            response.addCookie(jwtService.createAccessTokenCookie(null, request.isSecure()));
            response.addCookie(jwtService.createRefreshTokenCookie(null, request.isSecure()));

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            response.addCookie(jwtService.createAccessTokenCookie(null, request.isSecure()));
            response.addCookie(jwtService.createRefreshTokenCookie(null, request.isSecure()));
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}

