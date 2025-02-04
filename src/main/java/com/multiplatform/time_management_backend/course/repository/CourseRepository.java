package com.multiplatform.time_management_backend.course.repository;

import com.multiplatform.time_management_backend.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
