package com.multiplatform.time_management_backend.department.repository;

import com.multiplatform.time_management_backend.user.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
