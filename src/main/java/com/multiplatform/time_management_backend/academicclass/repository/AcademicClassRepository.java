package com.multiplatform.time_management_backend.academicclass.repository;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.user.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicClassRepository extends JpaRepository<AcademicClass, Long> {
    List<AcademicClass> findAllBySemester(Semester semester);

    List<AcademicClass> findAllByTeacher(Teacher teacher);
}
