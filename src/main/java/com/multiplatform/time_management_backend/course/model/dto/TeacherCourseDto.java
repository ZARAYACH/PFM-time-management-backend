package com.multiplatform.time_management_backend.course.model.dto;

import jakarta.validation.constraints.NotNull;

public record TeacherCourseDto(
        @NotNull Long id,
        Long courseId,
        Long teacherId,
        Long academicSemesterId
) {
}
