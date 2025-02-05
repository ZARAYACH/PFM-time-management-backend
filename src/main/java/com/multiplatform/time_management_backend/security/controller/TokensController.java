package com.multiplatform.time_management_backend.security.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.multiplatform.time_management_backend.exeption.AuthenticationServiceUnavailableException;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.security.jwt.JwtService;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Tag(name = "Tokens")
public class TokensController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping
    private Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws NotFoundException {

        String refreshToken = jwtService.extractRefreshToken(request);
        DecodedJWT decodedRefreshToken = jwtService.validateRefreshToken(refreshToken);
        UserDetails userDetails = userService.findByEmail(decodedRefreshToken.getSubject());

        String token = null;
        String newRefreshToken = null;

        try {
            token = jwtService.generateAccessToken(userDetails, decodedRefreshToken.getClaim(JwtService.SESSION_ID_CLAIM_NAME).asString());
            jwtService.blackListRefreshToken(decodedRefreshToken.getId());
            newRefreshToken = jwtService.generateRefreshToken(userDetails, decodedRefreshToken.getClaim(JwtService.SESSION_ID_CLAIM_NAME).asString());
        } catch (BadArgumentException e) {
            throw new AuthenticationServiceUnavailableException(e);
        }

        jwtService.extendSessionExpirationWindowAsync(decodedRefreshToken.getClaim(JwtService.SESSION_ID_CLAIM_NAME).asString());

        response.addCookie(jwtService.createAccessTokenCookie(token, request.isSecure()));
        response.addCookie(jwtService.createRefreshTokenCookie(newRefreshToken, request.isSecure()));

        return Map.of("access_token", token);
    }
}
