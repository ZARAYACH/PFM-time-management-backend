package com.multiplatform.time_management_backend.course.controller;

import com.multiplatform.time_management_backend.course.CourseMapper;
import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.course.model.dto.CourseDto;
import com.multiplatform.time_management_backend.course.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Course")
public class CourseController {

    private final CourseMapper courseMapper;
    private final CourseService courseService;

    @GetMapping
    private List<CourseDto> list() {
        return courseMapper.toCourseDto(courseService.list());
    }

    @GetMapping("/{id}")
    private CourseDto findById(@PathVariable long id) throws NotFoundException {
        return courseMapper.toCourseDto(courseService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<CourseDto> findByIds(@PathVariable Set<Long> ids) {
        return courseMapper.toCourseDto(courseService.findById(ids));
    }

    @PostMapping
    private CourseDto create(@RequestBody CourseDto courseDto) throws NotFoundException, BadArgumentException {
        return courseMapper.toCourseDto(courseService.create(courseDto));
    }

    @PostMapping("/{id}")
    private CourseDto modify(@PathVariable long id, @RequestBody CourseDto courseDto) throws NotFoundException, BadArgumentException {
        Course course = courseService.findById(id);
        return courseMapper.toCourseDto(courseService.modify(course, courseDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        Course course = courseService.findById(id);
        courseService.delete(course);
        return Collections.singletonMap("deleted", true);
    }

}
