package com.multiplatform.time_management_backend.timetable.controller;

import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.service.SemesterService;
import com.multiplatform.time_management_backend.timetable.TimeTableMapper;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.service.TimeTableGenerator;
import com.multiplatform.time_management_backend.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time-tables")
@RequiredArgsConstructor
@Tag(name = "TimeTables")
public class TimeTableController {

    private final TimeTableService timeTableService;
    private final TimeTableMapper timeTableMapper;
    private final TimeTableGenerator timeTableGenerator;
    private final SemesterService semesterService;

    @GetMapping
    private List<TimeTableDto> list() {
        return timeTableMapper.toTimeTableDto(timeTableService.list());
    }

    @GetMapping("/generate")
    private List<TimeTableGenerator.TimeTableSet> generateTimeTables() {
        List<Semester> semesters = semesterService.list();
        return semesters.stream().map(semester -> timeTableGenerator.generateTimeTables(semester,50, 100)).toList();
    }
}
