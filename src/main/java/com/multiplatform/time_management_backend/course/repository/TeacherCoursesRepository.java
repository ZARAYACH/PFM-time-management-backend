package com.multiplatform.time_management_backend.course.repository;

import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherCoursesRepository extends JpaRepository<TeacherCourse, Long> {
}
