package com.multiplatform.time_management_backend.timetable.service;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.repository.AcademicClassRepository;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
import com.multiplatform.time_management_backend.semester.repository.SemesterRepository;
import com.multiplatform.time_management_backend.timetable.modal.Day;
import com.multiplatform.time_management_backend.timetable.modal.TimeSlot;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.repository.TimeTableRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final SemesterRepository semesterRepository;
    private final GroupRepository groupRepository;
    private final AcademicClassRepository academicClassRepository;

    public List<TimeTable> list() {
        return timeTableRepository.findAll();
    }

    public TimeTable findById(long id) throws NotFoundException {
        return timeTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TimeTable with id " + id + " not found"));
    }

    public List<TimeTable> findById(Set<Long> ids) {
        return timeTableRepository.findAllById(ids);
    }

    public TimeTable create(TimeTableDto timeTableDto) throws NotFoundException {
        TimeTable timeTable = validateTimeTableDtoAndCreate(timeTableDto);
        return timeTableRepository.save(timeTable);
    }

    public TimeTable modify(TimeTable timeTable, TimeTableDto timeTableDto) throws NotFoundException {
        TimeTable newTimeTable = validateTimeTableDtoAndCreate(timeTableDto);
        timeTable.setDays(newTimeTable.getDays());
        timeTable.setSemester(newTimeTable.getSemester());
        timeTable.setGroup(newTimeTable.getGroup());
        return timeTableRepository.save(timeTable);
    }

    private TimeTable validateTimeTableDtoAndCreate(TimeTableDto timeTableDto) throws NotFoundException {
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semesterRepository.findById(timeTableDto.semesterId()).orElseThrow(() -> new NotFoundException("Semester not found")));
        timeTable.setGroup(groupRepository.findById(timeTableDto.groupId()).orElseThrow(() -> new NotFoundException("Group not found")));
        validateDays(timeTableDto.groupId(), timeTableDto.days());
        timeTable.setDays(timeTableDto.days());
        return timeTable;
    }

    private void validateDays(Long groupId, @NotNull Map<DayOfWeek, Day> days) throws NotFoundException {
        for (Day day : days.values()) {
            for (TimeSlot slot : day.timeSlots().values()) {
                AcademicClass academicClass = academicClassRepository.findById(slot.academicClassId()).orElseThrow(() -> new NotFoundException("academic class not found"));
                Assert.isTrue(academicClass.getGroup().getId().equals(groupId), "Academic class is not for group" + groupId);
            }
        }

    }

    public void delete(TimeTable timeTable) {
        timeTable.setGroup(null);
        timeTable.setSemester(null);
        timeTableRepository.delete(timeTable);
    }
}
