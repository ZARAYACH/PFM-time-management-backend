package com.multiplatform.time_management_backend.timetable.service;

import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

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

}
