package com.multiplatform.time_management_backend.semester.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.semester.SemesterMapper;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.modal.dto.AcademicSemesterDto;
import com.multiplatform.time_management_backend.semester.service.AcademicSemesterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/academic-semesters")
@RequiredArgsConstructor
@Tag(name = "AcademicSemester")
public class AcademicSemesterController {

    private final SemesterMapper semesterMapper;
    private final AcademicSemesterService academicSemesterService;

    @GetMapping
    private List<AcademicSemesterDto> list() {
        return semesterMapper.toAcademicSemesterDto(academicSemesterService.list());
    }

    @GetMapping("/{id}")
    private AcademicSemesterDto findById(@PathVariable long id) throws NotFoundException {
        return semesterMapper.toAcademicSemesterDto(academicSemesterService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<AcademicSemesterDto> findByIds(@PathVariable Set<Long> ids) {
        return semesterMapper.toAcademicSemesterDto(academicSemesterService.findById(ids));
    }

    @PostMapping
    private AcademicSemesterDto create(@RequestBody AcademicSemesterDto academicSemesterDto) throws NotFoundException, BadArgumentException {
        return semesterMapper.toAcademicSemesterDto(academicSemesterService.create(academicSemesterDto));
    }

    @PostMapping("/{id}")
    private AcademicSemesterDto modify(@PathVariable long id, @RequestBody AcademicSemesterDto academicSemesterDto) throws NotFoundException, BadArgumentException {
        AcademicSemester academicSemester = academicSemesterService.findById(id);
        return semesterMapper.toAcademicSemesterDto(academicSemesterService.modify(academicSemester, academicSemesterDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        AcademicSemester academicSemester = academicSemesterService.findById(id);
        academicSemesterService.delete(academicSemester);
        return Collections.singletonMap("deleted", true);
    }
}