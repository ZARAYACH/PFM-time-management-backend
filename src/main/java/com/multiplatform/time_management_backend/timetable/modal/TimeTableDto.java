package com.multiplatform.time_management_backend.timetable.modal;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record TimeTableDto(
        @NotNull Long id,
        @NotNull Long semesterId,
        @NotNull Long groupId,
        @NotNull String groupName,
        @NotNull Map<DayOfWeek, Day> days
) {
}


