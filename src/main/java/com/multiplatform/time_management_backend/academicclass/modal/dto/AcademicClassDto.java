package com.multiplatform.time_management_backend.academicclass.modal.dto;


import jakarta.validation.constraints.NotNull;

public record AcademicClassDto(
        @NotNull Long id,
        @NotNull Long semesterId,
        @NotNull String groupName,
        @NotNull Long groupId,
        @NotNull String teacherName,
        @NotNull Long teacherId,
        @NotNull String courseName,
        @NotNull Long courseId
) {
}
