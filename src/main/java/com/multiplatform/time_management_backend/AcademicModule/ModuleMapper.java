package com.multiplatform.time_management_backend.AcademicModule;


import com.multiplatform.time_management_backend.AcademicModule.model.AcademicModule;
import com.multiplatform.time_management_backend.AcademicModule.model.dto.ModuleDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface ModuleMapper {

    ModuleDto toModuleDto(AcademicModule academicModule);

    List<ModuleDto> toModuleDto(List<AcademicModule> academicModules);

}
