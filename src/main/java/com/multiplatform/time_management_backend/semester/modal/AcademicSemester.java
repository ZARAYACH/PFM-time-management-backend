package com.multiplatform.time_management_backend.semester.modal;


import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.AcademicModule.model.AcademicModule;
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
@AllArgsConstructor
public class AcademicSemester {

    @Id
    private Long id;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private Group group;

    @ManyToMany
    @JoinTable(name = "semesterModules")
    private List<AcademicModule> academicModules;

}
