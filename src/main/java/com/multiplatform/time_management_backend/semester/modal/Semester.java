package com.multiplatform.time_management_backend.semester.modal;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    private int year;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<AcademicClass> academicClass;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<TimeTable> timeTables;

    public enum Type {
        FALL, SPRING
    }

    public Semester(Type type, int year) {
        this.type = type;
        this.year = year;
    }
}
