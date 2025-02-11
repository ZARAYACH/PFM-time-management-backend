package com.multiplatform.time_management_backend.academicclass;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.modal.dto.AcademicClassDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AcademicClassMapper {

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "teacher.id", target = "teacherId")
    AcademicClassDto toAcademicClassDto(AcademicClass academicSemesters);

    List<AcademicClassDto> toAcademicClassDto(List<AcademicClass> academicClasses);

}
