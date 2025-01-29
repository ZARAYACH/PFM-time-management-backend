package com.multiplatform.time_management_backend.AcademicModule.service;

import com.multiplatform.time_management_backend.AcademicModule.model.AcademicModule;
import com.multiplatform.time_management_backend.AcademicModule.model.dto.ModuleDto;
import com.multiplatform.time_management_backend.AcademicModule.repository.AcademicModuleRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AcademicModuleService {

    private final AcademicModuleRepository academicModuleRepository;
    private final TeacherService teacherService;

    public List<AcademicModule> list() {
        return academicModuleRepository.findAll();
    }

    public AcademicModule findById(long id) throws NotFoundException {
        return academicModuleRepository.findById(id).orElseThrow(() -> new NotFoundException("Module with id " + id + " Not found"));
    }

    public List<AcademicModule> findById(Set<Long> ids) {
        return academicModuleRepository.findAllById(ids);
    }


    public AcademicModule create(ModuleDto moduleDto) throws NotFoundException, BadArgumentException {
        AcademicModule academicModule = validateModuleDtoAndCreate(moduleDto);
        return academicModuleRepository.save(academicModule);
    }

    private AcademicModule validateModuleDtoAndCreate(ModuleDto moduleDto) throws BadArgumentException {
        try {
            Assert.hasText(moduleDto.name(), "Module name cannot be empty");
            Assert.notEmpty(moduleDto.teacherIds(), "Module teacherIds cannot be empty");
            List<Teacher> teachers = teacherService.findById(moduleDto.teacherIds());
            Assert.notEmpty(teachers, "Module teachers cannot be empty");
            return new AcademicModule(null, moduleDto.name(), teachers, null);
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }

    public AcademicModule modify(AcademicModule academicModule, ModuleDto moduleDto) throws BadArgumentException {
        AcademicModule newAcademicModule = validateModuleDtoAndCreate(moduleDto);
        academicModule.setName(newAcademicModule.getName());
        academicModule.setTeachers(newAcademicModule.getTeachers());
        return academicModuleRepository.save(academicModule);
    }

    public void delete(AcademicModule academicModule) {
        academicModule.setTeachers(null);
        academicModuleRepository.delete(academicModule);
    }
}
