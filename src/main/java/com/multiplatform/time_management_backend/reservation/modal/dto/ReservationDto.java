package com.multiplatform.time_management_backend.reservation.modal.dto;

import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.room.model.dto.ClassRoomDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record ReservationDto(
        @NotNull Long id,
        Long classroomId,
        Long classId,
        String groupName,
        String classRoomNumber,
        String teacherName,
        String className,
        String teacherEmail,
        String courseName,
        String reservedBy,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Reservation.Type type,
        Integer recurrenceCount,
        LocalDateTime recurrenceEndDate,
        boolean recurring,
        Long parentReservationId,
        Set<Long> childReservationIds
) {
}
