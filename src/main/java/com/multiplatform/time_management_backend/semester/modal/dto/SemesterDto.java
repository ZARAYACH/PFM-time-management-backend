package com.multiplatform.time_management_backend.semester.modal.dto;

import com.multiplatform.time_management_backend.semester.modal.Semester;
import jakarta.validation.constraints.NotNull;

import java.time.Year;

public record SemesterDto(
        @NotNull Long id,
        @NotNull Semester.Type type,
        @NotNull int year
) {
}
