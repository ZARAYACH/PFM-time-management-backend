package com.multiplatform.time_management_backend.timetable.modal;

import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.timetable.TimeTableConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TimeTableConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private TimeTableDto timeTable;

    @OneToOne(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private AcademicSemester academicSemester;
}
