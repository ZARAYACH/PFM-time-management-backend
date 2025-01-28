package com.multiplatform.time_management_backend.department;

import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.department.model.dto.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface DepartmentMapper {

    List<DepartmentDto> toDepartmentDto(List<Department> departments);
    DepartmentDto toDepartmentDto(Department department);

}
