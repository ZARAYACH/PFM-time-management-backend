package com.multiplatform.time_management_backend.course.service;

import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.course.model.dto.CourseDto;
import com.multiplatform.time_management_backend.course.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;

    public List<Course> list() {
        return courseRepository.findAll();
    }

    public Course findById(long id) throws NotFoundException {
        return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course with id " + id + " Not found"));
    }

    public List<Course> findById(Set<Long> ids) {
        return courseRepository.findAllById(ids);
    }


    public Course create(CourseDto courseDto) throws NotFoundException, BadArgumentException {
        Course course = validateCourseDtoAndCreate(courseDto);
        return courseRepository.save(course);
    }

    private Course validateCourseDtoAndCreate(CourseDto courseDto) throws BadArgumentException {
        try {
            Assert.hasText(courseDto.name(), "Course name cannot be empty");
            Assert.notEmpty(courseDto.teacherIds(), "Course teacherIds cannot be empty");
            return new Course(null, courseDto.name(), courseDto.classRoomType(), null);
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }

    public Course modify(Course course, CourseDto courseDto) throws BadArgumentException {
        Course newCourse = validateCourseDtoAndCreate(courseDto);
        course.setName(newCourse.getName());
        return courseRepository.save(course);
    }

    public void delete(Course course) {
        courseRepository.delete(course);
    }
}
