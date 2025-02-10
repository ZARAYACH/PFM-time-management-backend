package com.multiplatform.time_management_backend.academicclass.modal;


import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.user.model.Teacher;
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
public class AcademicClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Semester semester;
    @ManyToOne(cascade = CascadeType.ALL)
    private Group group;
    @ManyToOne(cascade = CascadeType.ALL)
    private Teacher teacher;
    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;
    @ManyToOne(cascade = CascadeType.ALL)
    private ClassRoom allowedRoom;

    public AcademicClass(Long id) {
        this.id = id;
    }

    public boolean isAllowedRoom(int roomID) {
        return allowedRoom != null && allowedRoom.getId() == roomID;
    }

}
