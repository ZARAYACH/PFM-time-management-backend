package com.multiplatform.time_management_backend.user.repository;

import com.multiplatform.time_management_backend.user.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
