package com.multiplatform.time_management_backend.module;


import com.multiplatform.time_management_backend.module.model.Module;
import com.multiplatform.time_management_backend.module.model.dto.ModuleDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface ModuleMapper {

    ModuleDto toModuleDto(Module module);

    List<ModuleDto> toModuleDto(List<Module> modules);

}
