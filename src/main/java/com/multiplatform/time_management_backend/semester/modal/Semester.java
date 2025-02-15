package com.multiplatform.time_management_backend.semester.modal;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "semester")
@Getter
@Setter
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type = Type.FALL;
    @Column(name = "academic_year", nullable = false)
    private Integer year;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicClass> academicClasses = new ArrayList<>();

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTable> timeTables = new ArrayList<>();

    public enum Type {
        FALL, SPRING
    }

    public Semester(Type type, Integer year, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
