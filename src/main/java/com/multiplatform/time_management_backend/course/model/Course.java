package com.multiplatform.time_management_backend.course.model;

import com.multiplatform.time_management_backend.room.model.ClassRoom;
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
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ClassRoom.Type classRoomType;

    @OneToMany(mappedBy = "course")
    private List<TeacherCourse> teacherCourses;


}
