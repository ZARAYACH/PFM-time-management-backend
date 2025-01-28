package com.multiplatform.time_management_backend.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.multiplatform.time_management_backend.module.model.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
