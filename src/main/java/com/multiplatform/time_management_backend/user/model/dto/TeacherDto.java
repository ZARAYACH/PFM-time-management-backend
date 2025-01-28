package com.multiplatform.time_management_backend.user.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TeacherDto(
        @NotNull
        Long id,
        @NotNull
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate
) {
}
