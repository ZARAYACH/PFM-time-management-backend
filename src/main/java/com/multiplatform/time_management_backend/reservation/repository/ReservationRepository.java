package com.multiplatform.time_management_backend.reservation.repository;

import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.classroom.id = :classroomId " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findConflictingReservations(Long classroomId, LocalDateTime startTime, LocalDateTime endTime);

    List<Reservation> findByParentReservationAndStartTimeAfter(Reservation existingReservation, LocalDateTime now);
}
