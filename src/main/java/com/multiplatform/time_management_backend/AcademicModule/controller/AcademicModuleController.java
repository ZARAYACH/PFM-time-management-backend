package com.multiplatform.time_management_backend.AcademicModule.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.AcademicModule.ModuleMapper;
import com.multiplatform.time_management_backend.AcademicModule.model.AcademicModule;
import com.multiplatform.time_management_backend.AcademicModule.model.dto.ModuleDto;
import com.multiplatform.time_management_backend.AcademicModule.service.AcademicModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
@Tag(name = "Module")
public class AcademicModuleController {

    private final ModuleMapper moduleMapper;
    private final AcademicModuleService academicModuleService;

    @GetMapping
    private List<ModuleDto> list() {
        return moduleMapper.toModuleDto(academicModuleService.list());
    }

    @GetMapping("/{id}")
    private ModuleDto findById(@PathVariable long id) throws NotFoundException {
        return moduleMapper.toModuleDto(academicModuleService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<ModuleDto> findByIds(@PathVariable Set<Long> ids) {
        return moduleMapper.toModuleDto(academicModuleService.findById(ids));
    }

    @PostMapping
    private ModuleDto create(@RequestBody ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        return moduleMapper.toModuleDto(academicModuleService.create(moduleDto));
    }

    @PostMapping("/{id}")
    private ModuleDto modify(@PathVariable long id, @RequestBody ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        AcademicModule academicModule = academicModuleService.findById(id);
        return moduleMapper.toModuleDto(academicModuleService.modify(academicModule, moduleDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        AcademicModule academicModule = academicModuleService.findById(id);
        academicModuleService.delete(academicModule);
        return Collections.singletonMap("deleted", true);
    }

}
