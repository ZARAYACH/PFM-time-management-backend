package com.multiplatform.time_management_backend.department.model;

import com.multiplatform.time_management_backend.room.model.ClassRoom;
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
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "chief_id")
    private Teacher chief;


    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<ClassRoom> classRooms = new ArrayList<>();


}
