package com.multiplatform.time_management_backend.room.model;

import com.multiplatform.time_management_backend.department.model.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private Long capacity;
    private boolean emphie;
    @ManyToOne
    private Department department;


}
