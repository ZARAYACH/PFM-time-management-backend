package com.multiplatform.time_management_backend.timetable.repository;

import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findAllBySemester(Semester semester);

    List<TimeTable> findAllByGroup(Group group);
}
