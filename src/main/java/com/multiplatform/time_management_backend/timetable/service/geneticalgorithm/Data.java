package com.multiplatform.time_management_backend.timetable.service.geneticalgorithm;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.user.model.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class Data {

    private final Semester semester;
    private final Group[] studentsGroups;
    private final Teacher[] instructors;
    private final ClassRoom[] rooms;
    private final Course[] courses;
    private final int periodsPerDay;
    private final int daysPerWeek;
    private final List<DayOfWeek> workingDays;
    private final AcademicClass[] classes;

    public Data(Semester semester, Group[] studentsGroups, Teacher[] instructors, ClassRoom[] rooms, Course[] courses, int periodsPerDay, List<DayOfWeek> workingDays, AcademicClass[] classes) {
        this.semester = semester;
        this.studentsGroups = studentsGroups;
        this.instructors = instructors;
        this.rooms = rooms;
        this.courses = courses;
        this.periodsPerDay = periodsPerDay;
        this.workingDays = workingDays;
        this.daysPerWeek = workingDays.size();
        this.classes = classes;
    }


    public AcademicClass getClassByID(int id) {
        for (AcademicClass cls : classes) {
            if (cls.getId() == id) {
                return cls;
            }
        }
        return new AcademicClass((long) -1);
    }

    public ClassRoom getRoomByID(int id) {
        for (ClassRoom room : this.rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }
}
