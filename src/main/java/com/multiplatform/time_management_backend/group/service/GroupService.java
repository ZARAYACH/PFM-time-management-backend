package com.multiplatform.time_management_backend.group.service;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.group.model.dto.GroupDto;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
import com.multiplatform.time_management_backend.user.model.Student;
import com.multiplatform.time_management_backend.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public List<Group> list() {
        return groupRepository.findAll();
    }

    public Group findById(long id) throws NotFoundException {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException("group with id " + id + " Not found"));
    }

    public List<Group> findById(Set<Long> ids) {
        return groupRepository.findAllById(ids);
    }


    public Group create(GroupDto groupDto) throws NotFoundException, BadArgumentException {
        Group group = validateGroupDtoAndCreate(groupDto);
        return groupRepository.save(group);
    }

    private Group validateGroupDtoAndCreate(GroupDto groupDto) throws BadArgumentException {
        try {
            Assert.hasText(groupDto.name(), "group name cannot be empty");
            Assert.notEmpty(groupDto.studentIds(), "group studentIds cannot be empty");
            List<Student> students = studentRepository.findAllById(groupDto.studentIds());
            Assert.notEmpty(students, "group teachers cannot be empty");
            Group group = new Group(null, groupDto.name(), null, null);
            students.forEach(student -> student.setGroup(group));
            group.setStudents(students);
            return group;
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }


    public Group modify(Group group, GroupDto groupDto) throws BadArgumentException {
        Group newgroup = validateGroupDtoAndCreate(groupDto);
        group.setName(newgroup.getName());
        group.setStudents(newgroup.getStudents());
        group.setAcademicSemesters(newgroup.getAcademicSemesters());
        return groupRepository.save(group);
    }

    public void delete(Group group) {
        group.setStudents(null);
        group.setAcademicSemesters(null);
        groupRepository.delete(group);
    }
}
