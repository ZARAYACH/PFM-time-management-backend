package com.multiplatform.time_management_backend.module.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ModuleDto(
        @NotNull Long id,
        @NotBlank String name,
        Set<Long> teacherIds
) {
}
