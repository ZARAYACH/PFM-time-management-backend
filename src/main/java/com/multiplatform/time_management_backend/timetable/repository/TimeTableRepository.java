package com.multiplatform.time_management_backend.timetable.repository;

import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
}
