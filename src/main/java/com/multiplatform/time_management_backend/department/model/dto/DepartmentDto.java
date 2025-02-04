package com.multiplatform.time_management_backend.department.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record DepartmentDto(
        @NotNull(message = "Department id cannot be null") Long id,
        @NotNull(message = "Department name cannot be null") String name,
        @NotNull(message = "Department chief cannot be null") Long chiefId,
        Set<Long> roomIds
) {
}
