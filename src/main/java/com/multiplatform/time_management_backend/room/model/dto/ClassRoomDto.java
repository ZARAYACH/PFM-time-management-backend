package com.multiplatform.time_management_backend.room.model.dto;

import com.multiplatform.time_management_backend.room.model.ClassRoom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassRoomDto(
        @NotNull Long id,
        String name,
        @NotBlank String classNumber,
        @NotNull Long capacity,
        @NotNull ClassRoom.Type type,
        boolean amphie,
        Long departmentId
) {
}
