package com.multiplatform.time_management_backend.module.service;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.module.model.Module;
import com.multiplatform.time_management_backend.module.model.dto.ModuleDto;
import com.multiplatform.time_management_backend.module.repository.ModuleRepository;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final TeacherService teacherService;

    public List<Module> list() {
        return moduleRepository.findAll();
    }

    public Module findById(long id) throws NotFoundException {
        return moduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Module with id " + id + " Not found"));
    }

    public List<Module> findById(Set<Long> ids) {
        return moduleRepository.findAllById(ids);
    }


    public Module create(ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        Module module = validateModuleDtoAndCreate(moduleDto);
        return moduleRepository.save(module);
    }

    private Module validateModuleDtoAndCreate(ModuleDto moduleDto) throws BadArgumentException {
        try {
            Assert.hasText(moduleDto.name(), "Module name cannot be empty");
            Assert.notEmpty(moduleDto.teacherIds(), "Module teacherIds cannot be empty");
            List<Teacher> teachers = teacherService.findById(moduleDto.teacherIds());
            Assert.notEmpty(teachers, "Module teachers cannot be empty");
            return moduleRepository.save(new Module(null, moduleDto.name(), teachers));
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }


    public Module modify(Module module, ModuleDto moduleDto) throws BadArgumentException {
        Module newModule = validateModuleDtoAndCreate(moduleDto);
        module.setName(newModule.getName());
        module.setTeachers(newModule.getTeachers());
        return moduleRepository.save(module);
    }

    public void delete(Module module) {
        module.setTeachers(null);
        moduleRepository.delete(module);
    }
}
