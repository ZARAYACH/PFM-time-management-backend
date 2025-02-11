package com.multiplatform.time_management_backend.room.model;

import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Long capacity;
    private boolean amphie;

    @ManyToOne
    private Department department;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classroom")
    private List<Reservation> reservations = new ArrayList<>();

    public enum Type {
        TP, COURSE, SEMINAR_ROOM
    }

}
