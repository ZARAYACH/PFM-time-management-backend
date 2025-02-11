package com.multiplatform.time_management_backend.academicclass.controller;

import com.multiplatform.time_management_backend.academicclass.AcademicClassMapper;
import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.modal.dto.AcademicClassDto;
import com.multiplatform.time_management_backend.academicclass.service.AcademicClassService;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/academic-classes")
@RequiredArgsConstructor
@Tag(name = "AcademicClass")
public class AcademicClassController {

    private final AcademicClassMapper academicClassMapper;
    private final AcademicClassService academicSemesterService;

    @GetMapping
    private List<AcademicClassDto> listAcademicSemester() {
        return academicClassMapper.toAcademicClassDto(academicSemesterService.list());
    }

    @GetMapping("/{id}")
    private AcademicClassDto findAcademicSemesterById(@PathVariable long id) throws NotFoundException {
        return academicClassMapper.toAcademicClassDto(academicSemesterService.findById(id));
    }

    @PostMapping
    private AcademicClassDto createAcademicSemester(@RequestBody AcademicClassDto academicClassDto) throws NotFoundException, BadArgumentException {
        return academicClassMapper.toAcademicClassDto(academicSemesterService.create(academicClassDto));
    }

    @PutMapping("/{id}")
    private AcademicClassDto modifyAcademicSemester(@PathVariable long id, @RequestBody AcademicClassDto academicClassDto) throws NotFoundException, BadArgumentException {
        AcademicClass academicClass = academicSemesterService.findById(id);
        return academicClassMapper.toAcademicClassDto(academicSemesterService.modify(academicClass, academicClassDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteAcademicSemester(@PathVariable long id) throws NotFoundException {
        AcademicClass academicClass = academicSemesterService.findById(id);
        academicSemesterService.delete(academicClass);
        return Collections.singletonMap("deleted", true);
    }
}