package com.multiplatform.time_management_backend.module.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.module.ModuleMapper;
import com.multiplatform.time_management_backend.module.model.Module;
import com.multiplatform.time_management_backend.module.model.dto.ModuleDto;
import com.multiplatform.time_management_backend.module.service.ModuleService;
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
public class ModuleController {

    private final ModuleMapper moduleMapper;
    private final ModuleService moduleService;

    @GetMapping
    private List<ModuleDto> list() {
        return moduleMapper.toModuleDto(moduleService.list());
    }

    @GetMapping("/{id}")
    private ModuleDto findById(@PathVariable long id) throws NotFoundException {
        return moduleMapper.toModuleDto(moduleService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<ModuleDto> findByIds(@PathVariable Set<Long> ids) {
        return moduleMapper.toModuleDto(moduleService.findById(ids));
    }

    @PostMapping
    private ModuleDto create(@RequestBody ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        return moduleMapper.toModuleDto(moduleService.create(moduleDto));
    }

    @PostMapping("/{id}")
    private ModuleDto modify(@PathVariable long id, @RequestBody ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        Module module = moduleService.findById(id);
        return moduleMapper.toModuleDto(moduleService.modify(module, moduleDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        Module module = moduleService.findById(id);
        moduleService.delete(module);
        return Collections.singletonMap("deleted", true);
    }

}
