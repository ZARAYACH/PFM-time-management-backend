package com.multiplatform.time_management_backend.semester.service;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.modal.dto.SemesterDto;
import com.multiplatform.time_management_backend.semester.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public List<Semester> list() {
        return semesterRepository.findAll();
    }

    public Semester findById(long id) throws NotFoundException {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Semester with id " + id + " Not found"));
    }

    public List<Semester> findById(Set<Long> ids) {
        return semesterRepository.findAllById(ids);
    }

    public Semester create(SemesterDto semesterDto) throws NotFoundException, BadArgumentException {
        Semester semester = validateSemesterDtoAndCreate(semesterDto);
        return semesterRepository.save(semester);
    }

    private Semester validateSemesterDtoAndCreate(SemesterDto semesterDto) throws BadArgumentException {
        try {
            Assert.notNull(semesterDto.year(), "Semester year cannot be null");
            Assert.isTrue(semesterDto.year() >= 1900, "Invalid semester year");
            Assert.notNull(semesterDto.type(), "Semester type cannot be null");
            Assert.notNull(semesterDto.startDate(), "Semester start date cannot be null");
            Assert.notNull(semesterDto.endDate(), "Semester end date cannot be null");
            Assert.isTrue(semesterDto.startDate().isBefore(semesterDto.endDate()), "Start date cannot be before end date");
            boolean exists = semesterRepository.existsByStartDateBeforeAndEndDateAfter(
                    semesterDto.endDate(), semesterDto.startDate());
            if (exists) {
                throw new BadArgumentException("There is already a semester within the given period.");
            }
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        return new Semester(semesterDto.type(), semesterDto.year(), semesterDto.startDate(), semesterDto.endDate());
    }


    public Semester modify(Semester semester, SemesterDto semesterDto) throws BadArgumentException {
        Semester newsemester = validateSemesterDtoAndCreate(semesterDto);
        semester.setYear(newsemester.getYear());
        semester.setType(newsemester.getType());
        return semesterRepository.save(semester);
    }

    public void delete(Semester semester) {
        semesterRepository.delete(semester);
    }
}
