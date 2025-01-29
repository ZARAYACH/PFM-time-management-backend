package com.multiplatform.time_management_backend.semester.modal.dto;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AcademicSemesterDto(
        @NotNull Long id,
        @NotNull Long semesterId,
        @NotNull Long groupId,
        List<Long> modulesIds
) {
}
