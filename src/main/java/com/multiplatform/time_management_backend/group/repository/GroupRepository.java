package com.multiplatform.time_management_backend.group.repository;

import com.multiplatform.time_management_backend.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
