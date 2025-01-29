package com.multiplatform.time_management_backend.semester;

import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.modal.dto.AcademicSemesterDto;
import com.multiplatform.time_management_backend.semester.modal.dto.SemesterDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface SemesterMapper {
    List<AcademicSemesterDto> toAcademicSemesterDto(List<AcademicSemester> academicSemesters);

    AcademicSemesterDto toAcademicSemesterDto(AcademicSemester academicSemesters);

    List<SemesterDto> toSemesterDto(List<Semester> semesters);

    SemesterDto toSemesterDto(Semester semesters);

}
