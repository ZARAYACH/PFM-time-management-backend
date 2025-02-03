package com.multiplatform.time_management_backend.semester.service;

import com.multiplatform.time_management_backend.course.repository.CourseRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.modal.dto.AcademicSemesterDto;
import com.multiplatform.time_management_backend.semester.repository.AcademicSemesterRepository;
import com.multiplatform.time_management_backend.semester.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AcademicSemesterService {

    private final AcademicSemesterRepository academicSemesterRepository;
    private final SemesterRepository semesterRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    public List<AcademicSemester> list() {
        return academicSemesterRepository.findAll();
    }

    public AcademicSemester findById(long id) throws NotFoundException {
        return academicSemesterRepository.findById(id).orElseThrow(() -> new NotFoundException("Academic semester with id " + id + " Not found"));
    }

    public List<AcademicSemester> findById(Set<Long> ids) {
        return academicSemesterRepository.findAllById(ids);
    }


    public AcademicSemester create(AcademicSemesterDto academicSemesterDto) throws NotFoundException, BadArgumentException {
        AcademicSemester academicSemester = validateAcademicSemesterDtoAndCreate(academicSemesterDto);
        return academicSemesterRepository.save(academicSemester);
    }

    private AcademicSemester validateAcademicSemesterDtoAndCreate(AcademicSemesterDto academicSemesterDto) throws BadArgumentException {
        AcademicSemester academicSemester = new AcademicSemester();
        try {
            Assert.notNull(academicSemesterDto.semesterId(), "Semester id cannot be null");
            Assert.notNull(academicSemesterDto.groupId(), "academicSemester groupId cannot be empty");

            academicSemester.setSemester(semesterRepository.findById(academicSemesterDto.semesterId())
                    .orElseThrow(() -> new IllegalArgumentException("Semester with id " + academicSemesterDto.semesterId() + " not found")));
            academicSemester.setGroup(groupRepository.findById(academicSemesterDto.groupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group with id " + academicSemesterDto.groupId() + " not found")));
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        return academicSemester;

    }


    public AcademicSemester modify(AcademicSemester academicSemester, AcademicSemesterDto academicSemesterDto) throws BadArgumentException {
        AcademicSemester newacademicSemester = validateAcademicSemesterDtoAndCreate(academicSemesterDto);
        academicSemester.setSemester(newacademicSemester.getSemester());
        academicSemester.setGroup(newacademicSemester.getGroup());
        return academicSemesterRepository.save(academicSemester);
    }

    public void delete(AcademicSemester academicSemester) {
        academicSemester.setSemester(null);
        academicSemester.setGroup(null);
        academicSemesterRepository.delete(academicSemester);
    }
}
