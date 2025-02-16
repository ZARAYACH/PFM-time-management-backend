package com.multiplatform.time_management_backend.statistics.service;

import com.multiplatform.time_management_backend.academicclass.repository.AcademicClassRepository;
import com.multiplatform.time_management_backend.course.repository.CourseRepository;
import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.room.repository.ClassRoomRepository;
import com.multiplatform.time_management_backend.statistics.StatisticsDto;
import com.multiplatform.time_management_backend.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AcademicClassRepository academicClassRepository;
    private final ClassRoomRepository classRoomRepository;

    public StatisticsDto getStatistics() {
        return new StatisticsDto(studentRepository.count(),
                teacherRepository.count(),
                courseRepository.count(),
                academicClassRepository.count(),
                classRoomRepository.count());
    }
}
