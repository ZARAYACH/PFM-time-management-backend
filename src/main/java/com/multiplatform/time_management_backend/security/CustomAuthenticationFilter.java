package com.multiplatform.time_management_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplatform.time_management_backend.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, AccessTokenService accessTokenService, RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
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
        String accessToken = jwtService.generateToken(user, accessTokenService);
        String refreshToken = jwtService.generateToken(user, refreshTokenService);
        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        response.setContentType(APPLICATION_JSON_VALUE);

        response.addCookie(jwtService.createTokenCookie(accessToken, request.isSecure(), accessTokenService));
        response.addCookie(jwtService.createTokenCookie(accessToken, request.isSecure(), refreshTokenService));
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //TODO: handle unsuccessful Authentication attempts
        throw failed;
    }


}
