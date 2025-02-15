package com.multiplatform.time_management_backend.timetable.modal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Slot {
    MORNING_FIRST(LocalTime.of(8, 30), LocalTime.of(10, 0)),
    MORNING_SECOND(LocalTime.of(10, 15), LocalTime.of(11, 45)),
    AFTERNOON_FIRST(LocalTime.of(14, 0), LocalTime.of(15, 30)),
    AFTERNOON_SECOND(LocalTime.of(15, 45), LocalTime.of(17, 15));
    private final LocalTime startTime;
    private final LocalTime endTime;

    public static Slot get(int i) {
        return switch (i) {
            case 0 -> MORNING_FIRST;
            case 1 -> MORNING_SECOND;
            case 2 -> AFTERNOON_FIRST;
            case 3 -> AFTERNOON_SECOND;
            default -> null;
        };
    }

    public static List<SlotDto> getDto() {
        return Arrays.stream(Slot.values()).map(slot -> new SlotDto(slot,
                slot.startTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                slot.endTime.format(DateTimeFormatter.ISO_LOCAL_TIME))).collect(Collectors.toList());
    }

    public record SlotDto(
           @NotNull Slot slot,
           @NotNull String startTime,
           @NotNull String endTime
    ) {

    }
}
