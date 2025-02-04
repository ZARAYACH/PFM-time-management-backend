package com.multiplatform.time_management_backend.timetable.modal;

import lombok.AllArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
public enum Slot {
    MORNING_FIRST(LocalTime.of(8, 30), LocalTime.of(10, 0)),
    MORNING_SECOND(LocalTime.of(10, 15), LocalTime.of(11, 45)),
    AFTERNOON_FIRST(LocalTime.of(14, 0), LocalTime.of(15, 30)),
    AFTERNOON_SECOND(LocalTime.of(15, 45), LocalTime.of(17, 15));

    private final LocalTime startTime;
    private final LocalTime endTime;

}
