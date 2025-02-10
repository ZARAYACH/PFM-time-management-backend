package com.multiplatform.time_management_backend.semester;

import com.multiplatform.time_management_backend.course.CourseMapper;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.modal.dto.SemesterDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
uses = {CourseMapper.class})
@Component
public interface SemesterMapper {

    List<SemesterDto> toSemesterDto(List<Semester> semesters);

    SemesterDto toSemesterDto(Semester semesters);

}
