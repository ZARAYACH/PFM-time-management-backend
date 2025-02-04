package com.multiplatform.time_management_backend.course.controller;

import com.multiplatform.time_management_backend.course.CourseMapper;
import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import com.multiplatform.time_management_backend.course.model.dto.TeacherCourseDto;
import com.multiplatform.time_management_backend.course.service.CourseService;
import com.multiplatform.time_management_backend.course.service.TeacherCoursesService;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/teacher-courses")
@RequiredArgsConstructor
@Tag(name = "TeacherCourses")
public class TeacherCoursesController {

    private final CourseMapper courseMapper;
    private final TeacherCoursesService teacherCoursesService;

    @GetMapping
    private List<TeacherCourseDto> listTeacherCourses() {
        return courseMapper.toTeacherCourseDto(teacherCoursesService.list());
    }

    @GetMapping("/{id}")
    private TeacherCourseDto findTeacherCoursesById(@PathVariable long id) throws NotFoundException {
        return courseMapper.toTeacherCourseDto(teacherCoursesService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<TeacherCourseDto> findTeacherCoursesByIds(@PathVariable Set<Long> ids) {
        return courseMapper.toTeacherCourseDto(teacherCoursesService.findById(ids));
    }

    @PostMapping
    private TeacherCourseDto createTeacherCourses(@RequestBody TeacherCourseDto teacherCourseDto) throws NotFoundException, BadArgumentException {
        return courseMapper.toTeacherCourseDto(teacherCoursesService.create(teacherCourseDto));
    }

    @PutMapping("/{id}")
    private TeacherCourseDto modifyTeacherCourses(@PathVariable long id, @RequestBody TeacherCourseDto teacherCourseDto) throws NotFoundException, BadArgumentException {
        TeacherCourse teacherCourse = teacherCoursesService.findById(id);
        return courseMapper.toTeacherCourseDto(teacherCoursesService.modify(teacherCourse, teacherCourseDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteTeacherCourses(@PathVariable long id) throws NotFoundException {
        TeacherCourse teacherCourse = teacherCoursesService.findById(id);
        teacherCoursesService.delete(teacherCourse);
        return Collections.singletonMap("deleted", true);
    }

}
