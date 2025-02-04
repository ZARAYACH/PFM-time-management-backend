package com.multiplatform.time_management_backend.course;


import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import com.multiplatform.time_management_backend.course.model.dto.CourseDto;
import com.multiplatform.time_management_backend.course.model.dto.TeacherCourseDto;
import com.multiplatform.time_management_backend.user.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface CourseMapper {
    CourseDto toCourseDto(Course course);

    List<CourseDto> toCourseDto(List<Course> courses);


    default Long map(Teacher teacher) {
        return teacher.getId();
    }

    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "academicSemester.id" , target = "academicSemesterId")

    TeacherCourseDto toTeacherCourseDto(TeacherCourse teacherCourse);

    List<TeacherCourseDto> toTeacherCourseDto(List<TeacherCourse> list);
}
