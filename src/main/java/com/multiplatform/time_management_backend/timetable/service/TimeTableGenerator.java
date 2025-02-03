package com.multiplatform.time_management_backend.timetable.service;

import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.course.model.CourseByTeacher;
import com.multiplatform.time_management_backend.course.service.CourseService;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.service.ClassRoomService;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.service.AcademicSemesterService;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.repository.TimeTableRepository;
import com.multiplatform.time_management_backend.user.model.Teacher;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimeTableGenerator {

    private final TimeTableRepository timeTableRepository;
    private final AcademicSemesterService academicSemesterService;
    private final CourseService courseService;
    private final ClassRoomService classRoomService;

    @Transactional
    public TimeTableDto generateRandomTimeTable(@NotNull AcademicSemester academicSemester) {
        TimeTableDto timeTable = TimeTableDto.getInstance();

        Group group = academicSemester.getGroup();
        List<CourseByTeacher> courseByTeachers = academicSemester.getCourseByTeachers();

        Map<DayOfWeek, Set<TimeTableDto.Day.TimeSlot.Slot>> usedSlots = new HashMap<>();

        for (CourseByTeacher courseByTeacher : courseByTeachers) {
            Course course = courseByTeacher.getCourse();
            Teacher teacher = courseByTeacher.getTeacher();
            ClassRoom classRoom = getRandomItem(classRoomService.findByType(course.getClassRoomType()));

            SlotInDay slotInDay = randomSlotInDay(new ArrayList<>(timeTable.days().keySet()), usedSlots);
            Set<TimeTableDto.Day.TimeSlot.Slot> slots = new HashSet<>();
            if (usedSlots.containsKey(slotInDay.dayOfWeek)) {
                slots = usedSlots.get(slotInDay.dayOfWeek);
            }
            slots.add(slotInDay.slot);
            usedSlots.put(slotInDay.dayOfWeek, slots);

            timeTable.days().get(slotInDay.dayOfWeek)
                    .timeSlots().put(slotInDay.slot, new TimeTableDto.Day.TimeSlot(teacher.getId(), course.getId(), classRoom.getId()));
        }
        return timeTable;
    }

    private <T> T getRandomItem(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    private SlotInDay randomSlotInDay(List<DayOfWeek> days, Map<DayOfWeek, Set<TimeTableDto.Day.TimeSlot.Slot>> usedSlots) {
        try {
            DayOfWeek day = getRandomItem(days);
            TimeTableDto.Day.TimeSlot.Slot slot = getRandomItem(Arrays.stream(TimeTableDto.Day.TimeSlot.Slot.values()).toList());
            if (usedSlots.containsKey(day) && usedSlots.get(day).contains(slot)) {
                throw new RuntimeException("used slot");
            }
            return new SlotInDay(day, slot);
        } catch (Exception e) {
            return randomSlotInDay(days, usedSlots);
        }
    }

    record SlotInDay(
            DayOfWeek dayOfWeek,
            TimeTableDto.Day.TimeSlot.Slot slot
    ) {
    }
}
