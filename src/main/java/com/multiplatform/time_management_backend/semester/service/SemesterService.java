package com.multiplatform.time_management_backend.semester.service;

import com.multiplatform.time_management_backend.AcademicModule.repository.AcademicModuleRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final AcademicModuleRepository moduleRepository;

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
            Assert.notNull(semesterDto.type(), "Semester type cannot be null");
            Assert.notNull(semesterDto.year(), "semester year cannot be empty");
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        return new Semester(null, semesterDto.type(), semesterDto.year(), null);
    }


    public Semester modify(Semester semester, SemesterDto semesterDto) throws BadArgumentException {
        Semester newsemester = validateSemesterDtoAndCreate(semesterDto);
        semester.setYear(newsemester.getYear());
        semester.setType(newsemester.getType());
        return semesterRepository.save(semester);
    }

    public void delete(Semester semester) {
        semester.setAcademicSemester(null);
        semesterRepository.delete(semester);
    }
}
