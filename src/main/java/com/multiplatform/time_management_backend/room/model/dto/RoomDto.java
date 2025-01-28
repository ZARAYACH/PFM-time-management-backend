package com.multiplatform.time_management_backend.room.model.dto;

import com.multiplatform.time_management_backend.department.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomDto(
        @NotNull Long id,
        String name,
        @NotBlank String number,
        @NotNull Long capacity,
        boolean emphie,
        Long departmentId
) {
}
