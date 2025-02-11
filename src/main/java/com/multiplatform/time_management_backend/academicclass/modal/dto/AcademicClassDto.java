package com.multiplatform.time_management_backend.academicclass.modal.dto;


import jakarta.validation.constraints.NotNull;

public record AcademicClassDto(
        @NotNull Long id,
        @NotNull Long semesterId,
        @NotNull Long groupId,
        @NotNull Long teacherId,
        @NotNull Long courseId
) {
}
