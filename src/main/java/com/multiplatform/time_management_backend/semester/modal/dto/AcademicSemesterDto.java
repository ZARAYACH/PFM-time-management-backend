package com.multiplatform.time_management_backend.semester.modal.dto;


import com.multiplatform.time_management_backend.course.model.dto.TeacherCourseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AcademicSemesterDto(
        @NotNull Long id,
        @NotNull Long semesterId,
        @NotNull Long groupId,
        @NotEmpty List<TeacherCourseDto> teacherCourse
) {
}
