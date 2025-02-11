package com.multiplatform.time_management_backend.timetable.modal;

import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.timetable.TimeTableConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"semester_id", "group_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Convert(converter = TimeTableConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private Map<DayOfWeek, Day> days;

    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;
}
