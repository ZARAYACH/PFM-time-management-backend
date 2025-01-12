package com.multiplatform.time_management_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.security.jwt.JwtService;
import com.multiplatform.time_management_backend.user.model.Session;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.repository.SessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, SessionRepository sessionRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Basic ")) {
            throw new AuthenticationCredentialsNotFoundException("Couldn't extract credentials From the request");
        }

        final String token = authHeader.substring(6);
        String[] emailPassword = new String(Base64.getDecoder().decode(token)).split(":", 2);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(emailPassword[0], emailPassword[1]);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        User user = (User) authentication.getPrincipal();

        Session session = new Session(user);
        sessionRepository.save(session);

        String accessToken = null;
        String refreshToken = null;

        try {
            accessToken = jwtService.generateAccessToken(user, session.getId());
            refreshToken = jwtService.generateRefreshToken(user, session.getId());

        } catch (BadArgumentException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        response.setContentType(APPLICATION_JSON_VALUE);

        response.addCookie(jwtService.createAccessTokenCookie(accessToken, request.isSecure()));
        response.addCookie(jwtService.createRefreshTokenCookie(refreshToken, request.isSecure()));

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //TODO: handle unsuccessful Authentication attempts
        throw failed;
    }


}
