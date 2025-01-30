package com.multiplatform.time_management_backend.room.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassRoomDto(
        @NotNull Long id,
        String name,
        @NotBlank String number,
        @NotNull Long capacity,
        boolean emphie,
        Long departmentId
) {
}
