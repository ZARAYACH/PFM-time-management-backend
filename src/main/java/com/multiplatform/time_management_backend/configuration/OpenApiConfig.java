package com.multiplatform.time_management_backend.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "basicAuth",
        scheme = "basic",
        description = "Basic Authentication",
        type = SecuritySchemeType.HTTP)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "jwt",
        scheme = "bearer",
        description = "Bearer Authentication",
        type = SecuritySchemeType.HTTP)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().paths(new Paths().addPathItem("/login", new PathItem()
                .post(new Operation()
                        .summary("User Login")
                        .description("Authenticate a user and retrieve an access token")
                        .addTagsItem("Authentication")
                        .operationId("login")
                        .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Successfully logged in and received access token"))
                                .addApiResponse("401", new ApiResponse()
                                        .description("Invalid credentials"))))));
    }
}