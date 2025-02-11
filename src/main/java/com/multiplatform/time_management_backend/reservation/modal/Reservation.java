package com.multiplatform.time_management_backend.reservation.modal;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reservedBy;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private ClassRoom classroom;

    @ManyToOne
    @JoinColumn(name = "academicClass_id")
    private AcademicClass academicClass;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Integer recurrenceCount;

    private LocalDateTime recurrenceEndDate;

    @OneToMany(mappedBy = "parentReservation", cascade = CascadeType.ALL)
    private List<Reservation> childReservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_reservation_id")
    private Reservation parentReservation;

    public boolean isRecurring() {
        return type != Type.NONE;
    }


    public enum Type {
        NONE,
        DAILY,
        WEEKLY,
        MONTHLY
    }
}

