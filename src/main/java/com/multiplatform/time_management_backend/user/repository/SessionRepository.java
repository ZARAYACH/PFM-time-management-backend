package com.multiplatform.time_management_backend.user.repository;

import com.multiplatform.time_management_backend.user.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}
