package com.multiplatform.time_management_backend.timetable.modal;

import jakarta.validation.constraints.NotNull;

public record TimeSlot(
        @NotNull Long classRoomId,
        @NotNull Long academicClassId) {
}
