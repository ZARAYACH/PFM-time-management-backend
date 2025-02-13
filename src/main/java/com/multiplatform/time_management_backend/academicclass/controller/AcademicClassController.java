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
    private final AcademicClassService academicClassService;

    @GetMapping
    private List<AcademicClassDto> listAcademicClass() {
        return academicClassMapper.toAcademicClassDto(academicClassService.list());
    }

    @GetMapping("/{id}")
    private AcademicClassDto findAcademicClassById(@PathVariable long id) throws NotFoundException {
        return academicClassMapper.toAcademicClassDto(academicClassService.findById(id));
    }

    @PostMapping
    private AcademicClassDto createAcademicClass(@RequestBody AcademicClassDto academicClassDto) throws NotFoundException, BadArgumentException {
        return academicClassMapper.toAcademicClassDto(academicClassService.create(academicClassDto));
    }

    @PutMapping("/{id}")
    private AcademicClassDto modifyAcademicClass(@PathVariable long id, @RequestBody AcademicClassDto academicClassDto) throws NotFoundException, BadArgumentException {
        AcademicClass academicClass = academicClassService.findById(id);
        return academicClassMapper.toAcademicClassDto(academicClassService.modify(academicClass, academicClassDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteAcademicClass(@PathVariable long id) throws NotFoundException {
        AcademicClass academicClass = academicClassService.findById(id);
        academicClassService.delete(academicClass);
        return Collections.singletonMap("deleted", true);
    }
}