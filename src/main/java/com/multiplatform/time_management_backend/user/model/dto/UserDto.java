package com.multiplatform.time_management_backend.user.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate
) {
    public record PostUserDto(
            String email,
            String password,
            String firstName,
            String lastName,
            LocalDate birthDate
    ) {
    }
}
