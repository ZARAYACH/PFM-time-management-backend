package com.multiplatform.time_management_backend.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDto(
        @NotNull
        Long id,
        @NotNull
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate
) {
    public record PostUserDto(
            @NotBlank
            String email,
            @NotBlank
            String password,
            String firstName,
            String lastName,
            LocalDate birthDate
    ) {
    }
}
