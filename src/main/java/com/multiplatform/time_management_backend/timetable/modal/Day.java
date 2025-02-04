package com.multiplatform.time_management_backend.timetable.modal;

import java.util.Map;

public record Day(
        Map<Slot, TimeSlot> timeSlots) {
}
