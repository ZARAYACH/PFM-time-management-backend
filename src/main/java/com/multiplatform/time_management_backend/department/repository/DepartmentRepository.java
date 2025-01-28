package com.multiplatform.time_management_backend.department.repository;

import com.multiplatform.time_management_backend.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
