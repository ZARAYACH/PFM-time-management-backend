package com.multiplatform.time_management_backend.AcademicModule.repository;

import com.multiplatform.time_management_backend.AcademicModule.model.AcademicModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicModuleRepository extends JpaRepository<AcademicModule, Long> {
}
