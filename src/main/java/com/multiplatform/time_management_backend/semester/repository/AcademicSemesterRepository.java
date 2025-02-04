package com.multiplatform.time_management_backend.semester.repository;

import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicSemesterRepository extends JpaRepository<AcademicSemester, Long> {
}
