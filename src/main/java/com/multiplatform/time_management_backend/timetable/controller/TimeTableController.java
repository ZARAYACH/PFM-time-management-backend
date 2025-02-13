package com.multiplatform.time_management_backend.timetable.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.service.SemesterService;
import com.multiplatform.time_management_backend.timetable.TimeTableMapper;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.service.TimeTableGenerator;
import com.multiplatform.time_management_backend.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    private List<TimeTableDto> listTimeTables() {
        return timeTableMapper.toTimeTableDto(timeTableService.list());
    }


    @GetMapping("/{id}")
    private TimeTableDto findTimeTableById(@PathVariable long id) throws NotFoundException {
        return timeTableMapper.toTimeTableDto(timeTableService.findById(id));
    }

    @PostMapping
    private TimeTableDto createTimeTable(@RequestBody TimeTableDto timeTableDto) throws NotFoundException {
        return timeTableMapper.toTimeTableDto(timeTableService.create(timeTableDto));

    }

    @PutMapping("/{id}")
    private TimeTableDto updateTimeTable(@PathVariable Long id, @RequestBody TimeTableDto timeTableDto) throws NotFoundException {
        TimeTable timeTable = timeTableService.findById(id);
        return timeTableMapper.toTimeTableDto(timeTableService.modify(timeTable, timeTableDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteTimeTable(@PathVariable Long id) throws NotFoundException {
        TimeTable timeTable = timeTableService.findById(id);
        timeTableService.delete(timeTable);
        return Map.of("deleted", Boolean.TRUE);
    }

    @PostMapping("/generate")
    private List<TimeTableDto> generateTimeTables(@RequestParam Long semesterId) throws BadArgumentException, NotFoundException {
        Semester semester = semesterService.findById(semesterId);
        return timeTableMapper.toTimeTableDto(timeTableGenerator.generateTimeTables(semester));
    }
}
