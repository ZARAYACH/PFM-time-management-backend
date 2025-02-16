package com.multiplatform.time_management_backend.statistics;

import com.multiplatform.time_management_backend.course.service.CourseService;
import com.multiplatform.time_management_backend.room.service.ClassRoomService;
import com.multiplatform.time_management_backend.statistics.service.StatisticsService;
import com.multiplatform.time_management_backend.user.model.Student;
import com.multiplatform.time_management_backend.user.service.StudentService;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.stat.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;


    @GetMapping
    private StatisticsDto getStatistics() {
        return statisticsService.getStatistics();
    }
}
