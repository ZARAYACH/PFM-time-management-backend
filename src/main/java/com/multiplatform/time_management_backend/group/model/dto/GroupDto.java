package com.multiplatform.time_management_backend.group.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupDto(
        @NotNull Long id,
        String name,
        Set<Long> studentIds
) {
}
