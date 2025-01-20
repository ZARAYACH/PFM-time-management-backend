package com.multiplatform.time_management_backend.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.security.jwt.JwtService;
import com.multiplatform.time_management_backend.user.model.Session;
import com.multiplatform.time_management_backend.user.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

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

            response.addCookie(new Cookie(JwtService.ACCESS_TOKEN_COOKIE_NAME, null));
            response.addCookie(new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, null));
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
