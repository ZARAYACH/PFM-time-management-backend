package com.multiplatform.time_management_backend.course.model.dto;

import com.multiplatform.time_management_backend.room.model.ClassRoom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CourseDto(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank ClassRoom.Type classRoomType,
        Set<Long> teacherIds
) {
}
