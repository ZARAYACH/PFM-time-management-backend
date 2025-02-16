package com.multiplatform.time_management_backend.academicclass.service;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.modal.dto.AcademicClassDto;
import com.multiplatform.time_management_backend.academicclass.repository.AcademicClassRepository;
import com.multiplatform.time_management_backend.course.repository.CourseRepository;
import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
import com.multiplatform.time_management_backend.semester.repository.SemesterRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AcademicClassService {

    private final AcademicClassRepository academicClassRepository;
    private final SemesterRepository semesterRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public List<AcademicClass> list() {
        return academicClassRepository.findAll();
    }

    public AcademicClass findById(long id) throws NotFoundException {
        return academicClassRepository.findById(id).orElseThrow(() -> new NotFoundException("Academic semester with id " + id + " Not found"));
    }

    public List<AcademicClass> findById(Set<Long> ids) {
        return academicClassRepository.findAllById(ids);
    }


    public AcademicClass create(AcademicClassDto academicClassDto) throws NotFoundException, BadArgumentException {
        AcademicClass academicClass = validateAcademicClassDtoAndCreate(academicClassDto);
        try {
            return academicClassRepository.save(academicClass);
        } catch (DataIntegrityViolationException e) {
            throw new BadArgumentException("Class already exists");
        }
    }

    private AcademicClass validateAcademicClassDtoAndCreate(AcademicClassDto academicClassDto) throws BadArgumentException {
        AcademicClass academicClass = new AcademicClass();
        try {
            Assert.notNull(academicClassDto.semesterId(), "Semester id cannot be null");
            Assert.notNull(academicClassDto.groupId(), "academicClass groupId cannot be empty");

            academicClass.setSemester(semesterRepository.findById(academicClassDto.semesterId())
                    .orElseThrow(() -> new IllegalArgumentException("Semester with id " + academicClassDto.semesterId() + " not found")));

            academicClass.setGroup(groupRepository.findById(academicClassDto.groupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group with id " + academicClassDto.groupId() + " not found")));

            academicClass.setTeacher(teacherRepository.findById(academicClassDto.teacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found")));

            academicClass.setCourse(courseRepository.findById(academicClassDto.courseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course with id " + academicClassDto.courseId() + " not found")));
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        return academicClass;
    }


    public AcademicClass modify(AcademicClass academicClass, AcademicClassDto academicClassDto) throws BadArgumentException {
        AcademicClass newacademicClass = validateAcademicClassDtoAndCreate(academicClassDto);
        academicClass.setSemester(newacademicClass.getSemester());
        academicClass.setGroup(newacademicClass.getGroup());
        return academicClassRepository.save(academicClass);
    }

    public void delete(AcademicClass academicClass) {
        academicClass.setSemester(null);
        academicClass.setGroup(null);
        academicClassRepository.delete(academicClass);
    }
}
