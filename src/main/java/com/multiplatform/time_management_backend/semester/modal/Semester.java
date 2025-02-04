package com.multiplatform.time_management_backend.semester.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    private int year;

    @OneToMany(mappedBy = "semester")
    private List<AcademicSemester> academicSemester;

    public enum Type {
        FALL, SPRING
    }
}
