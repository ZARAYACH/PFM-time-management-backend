package com.multiplatform.time_management_backend.semester.repository;

import com.multiplatform.time_management_backend.semester.modal.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
