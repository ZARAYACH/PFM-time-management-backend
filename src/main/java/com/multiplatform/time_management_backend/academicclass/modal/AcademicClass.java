package com.multiplatform.time_management_backend.academicclass.modal;


import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.user.model.Teacher;
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
public class AcademicClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Semester semester;
    @ManyToOne
    private Group group;
    @ManyToOne
    private Teacher teacher;
    @ManyToOne
    private Course course;
    @ManyToOne
    private ClassRoom allowedRoom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "academicClass")
    private List<Reservation> reservations = new ArrayList<>();

    public AcademicClass(Long id) {
        this.id = id;
    }

    public boolean isAllowedRoom(int roomID) {
        return allowedRoom != null && allowedRoom.getId() == roomID;
    }

}
