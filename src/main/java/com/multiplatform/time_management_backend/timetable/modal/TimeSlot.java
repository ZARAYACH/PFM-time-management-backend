package com.multiplatform.time_management_backend.timetable.modal;

public record TimeSlot(
        Long teacherId,
        Long courseId,
        Long classRoomId,
        Long groupId) {

}
