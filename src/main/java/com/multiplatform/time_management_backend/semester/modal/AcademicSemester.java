package com.multiplatform.time_management_backend.semester.modal;


import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import com.multiplatform.time_management_backend.group.model.Group;
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
@AllArgsConstructor
public class AcademicSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "academicSemester",cascade = CascadeType.ALL )
    private List<TeacherCourse> teacherCourse;

    @OneToOne(cascade = CascadeType.ALL)
    private TimeTable timeTable;

}
