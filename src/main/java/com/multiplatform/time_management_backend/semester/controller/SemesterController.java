package com.multiplatform.time_management_backend.semester.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.semester.SemesterMapper;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.modal.dto.SemesterDto;
import com.multiplatform.time_management_backend.semester.service.SemesterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
@Tag(name = "Semester")
public class SemesterController {

    private final SemesterMapper semesterMapper;
    private final SemesterService semesterService;

    @GetMapping
    private List<SemesterDto> list() {
        return semesterMapper.toSemesterDto(semesterService.list());
    }

    @GetMapping("/{id}")
    private SemesterDto findById(@PathVariable long id) throws NotFoundException {
        return semesterMapper.toSemesterDto(semesterService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<SemesterDto> findByIds(@PathVariable Set<Long> ids) {
        return semesterMapper.toSemesterDto(semesterService.findById(ids));
    }

    @PostMapping
    private SemesterDto create(@RequestBody SemesterDto semesterDto) throws NotFoundException, BadArgumentException {
        return semesterMapper.toSemesterDto(semesterService.create(semesterDto));
    }

    @PutMapping("/{id}")
    private SemesterDto modify(@PathVariable long id, @RequestBody SemesterDto semesterDto) throws NotFoundException, BadArgumentException {
        Semester semester = semesterService.findById(id);
        return semesterMapper.toSemesterDto(semesterService.modify(semester, semesterDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        Semester semester = semesterService.findById(id);
        semesterService.delete(semester);
        return Collections.singletonMap("deleted", true);
    }
}