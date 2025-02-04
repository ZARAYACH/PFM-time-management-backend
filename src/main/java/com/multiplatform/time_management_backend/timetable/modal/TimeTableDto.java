package com.multiplatform.time_management_backend.timetable.modal;

import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public record TimeTableDto(
        @NotNull Long groupId,
        @NotNull Long academicSemesterId,
        Map<DayOfWeek, Day> days
) {
    public static TimeTableDto getInstance(@NotNull AcademicSemester academicSemester,@NotNull Group group) {
        return new TimeTableDto(group.getId(), academicSemester.getId(),Map.of(DayOfWeek.MONDAY, new Day(new HashMap<>()),
                DayOfWeek.TUESDAY, new Day(new HashMap<>()),
                DayOfWeek.WEDNESDAY, new Day(new HashMap<>()),
                DayOfWeek.THURSDAY, new Day(new HashMap<>()),
                DayOfWeek.FRIDAY, new Day(new HashMap<>()),
                DayOfWeek.SATURDAY, new Day(new HashMap<>())));
    }
}


