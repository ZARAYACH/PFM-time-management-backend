package com.multiplatform.time_management_backend.course.service;

import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import com.multiplatform.time_management_backend.course.model.dto.TeacherCourseDto;
import com.multiplatform.time_management_backend.course.repository.CourseRepository;
import com.multiplatform.time_management_backend.course.repository.TeacherCoursesRepository;
import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.repository.AcademicSemesterRepository;
import com.multiplatform.time_management_backend.user.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherCoursesService {

    private final TeacherCoursesRepository teacherCoursesRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final AcademicSemesterRepository academicSemesterRepository;

    public List<TeacherCourse> list() {
        return teacherCoursesRepository.findAll();
    }

    public TeacherCourse findById(long id) throws NotFoundException {
        return teacherCoursesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher Course with id " + id + " Not found"));
    }

    public List<TeacherCourse> findById(Set<Long> ids) {
        return teacherCoursesRepository.findAllById(ids);
    }

    public TeacherCourse create(TeacherCourseDto teacherCourseDto) throws NotFoundException, BadArgumentException {
        TeacherCourse teacherCourse = validateTeacherCourseDtoAndCreate(teacherCourseDto);
        return teacherCoursesRepository.save(teacherCourse);
    }

    private TeacherCourse validateTeacherCourseDtoAndCreate(TeacherCourseDto teacherCourseDto) throws BadArgumentException {
        try {
            Teacher teacher = teacherRepository.findById(teacherCourseDto.teacherId()).orElseThrow(() -> new IllegalArgumentException("Teacher #" + teacherCourseDto.teacherId() + " Not found"));
            Course course = courseRepository.findById(teacherCourseDto.courseId()).orElseThrow(() -> new IllegalArgumentException("Course #" + teacherCourseDto.courseId() + "Not found"));

            AcademicSemester academicSemester = null;
            if (teacherCourseDto.academicSemesterId() != null) {
                academicSemester = academicSemesterRepository.findById(teacherCourseDto.academicSemesterId()).orElseThrow(() -> new IllegalArgumentException("Academic Semester Not found"));
            }
            return new TeacherCourse(null, course, teacher, academicSemester);
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }

    public TeacherCourse modify(TeacherCourse teacherCourse, TeacherCourseDto teacherCourseDto) throws BadArgumentException {
        TeacherCourse newTeacherCourse = validateTeacherCourseDtoAndCreate(teacherCourseDto);
        teacherCourse.setTeacher(newTeacherCourse.getTeacher());
        teacherCourse.setCourse(newTeacherCourse.getCourse());
        teacherCourse.setAcademicSemester(newTeacherCourse.getAcademicSemester());
        return teacherCoursesRepository.save(teacherCourse);
    }

    public void delete(TeacherCourse teacherCourse) {
        teacherCourse.setTeacher(null);
        teacherCourse.setCourse(null);
        teacherCourse.setAcademicSemester(null);
        teacherCoursesRepository.delete(teacherCourse);
    }

}
