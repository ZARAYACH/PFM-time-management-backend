package com.multiplatform.time_management_backend.timetable.controller;

import com.multiplatform.time_management_backend.timetable.TimeTableMapper;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time-tables")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService timeTableService;
    private final TimeTableMapper timeTableMapper;

    @GetMapping
    private List<TimeTableDto> list() {
        return timeTableMapper.toTimeTableDto(timeTableService.list());
    }
}
