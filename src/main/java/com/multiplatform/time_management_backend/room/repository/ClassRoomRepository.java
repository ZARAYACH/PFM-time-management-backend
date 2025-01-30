package com.multiplatform.time_management_backend.room.repository;

import com.multiplatform.time_management_backend.room.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
}
