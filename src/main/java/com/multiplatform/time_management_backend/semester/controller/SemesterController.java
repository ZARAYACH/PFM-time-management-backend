package com.multiplatform.time_management_backend.semester.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.reservation.ReservationMapper;
import com.multiplatform.time_management_backend.reservation.modal.dto.ReservationDto;
import com.multiplatform.time_management_backend.reservation.service.ReservationService;
import com.multiplatform.time_management_backend.semester.SemesterMapper;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.modal.dto.SemesterDto;
import com.multiplatform.time_management_backend.semester.service.SemesterService;
import com.multiplatform.time_management_backend.timetable.TimeTableMapper;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.service.TimeTableGenerator;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
@Tag(name = "Semester")
public class SemesterController {

    private final SemesterMapper semesterMapper;
    private final SemesterService semesterService;
    private final TimeTableGenerator timeTableGenerator;
    private final TimeTableMapper timeTableMapper;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final UserService userService;

    @GetMapping
    private List<SemesterDto> listSemester() {
        return semesterMapper.toSemesterDto(semesterService.list());
    }

    @GetMapping("/{id}")
    private SemesterDto findSemesterById(@PathVariable long id) throws NotFoundException {
        return semesterMapper.toSemesterDto(semesterService.findById(id));
    }

    @PostMapping
    private SemesterDto createSemester(@RequestBody SemesterDto semesterDto) throws NotFoundException, BadArgumentException {
        return semesterMapper.toSemesterDto(semesterService.create(semesterDto));
    }

    @PutMapping("/{id}")
    private SemesterDto modifySemester(@PathVariable long id, @RequestBody SemesterDto semesterDto) throws NotFoundException, BadArgumentException {
        Semester semester = semesterService.findById(id);
        return semesterMapper.toSemesterDto(semesterService.modify(semester, semesterDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteSemester(@PathVariable long id) throws NotFoundException {
        Semester semester = semesterService.findById(id);
        semesterService.delete(semester);
        return Collections.singletonMap("deleted", true);
    }

    @PostMapping("/{id}/generate-time-tables")
    private List<TimeTableDto> generateSemesterTimeTables(@PathVariable long id) throws NotFoundException, BadArgumentException {
        Semester semester = semesterService.findById(id);
        return timeTableMapper.toTimeTableDto(timeTableGenerator.generateTimeTables(semester));
    }

    @PostMapping("/{id}/reserve-class-rooms")
    private List<ReservationDto> reserveClassroomsForSemester(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException, BadArgumentException {
        Semester semester = semesterService.findById(id);
        User user = userService.findByEmail(userDetails.getUsername());
        return reservationMapper.toReservationDto(reservationService.reserveSemester(semester, user));
    }
}