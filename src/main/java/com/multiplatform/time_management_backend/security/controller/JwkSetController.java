package com.multiplatform.time_management_backend.security.controller;

import com.multiplatform.time_management_backend.security.jwt.JWKSetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "JwkSet")
public class JwkSetController {

    private final JWKSetService jwkSetService;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return this.jwkSetService.jwkSet().toJSONObject();
    }
}
