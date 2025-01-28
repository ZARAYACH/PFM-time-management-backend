package com.multiplatform.time_management_backend.user.service;

import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> list() {
        return teacherRepository.findAll();
    }

    public Teacher findById(long id) throws NotFoundException {
        return teacherRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Teacher with id" + id + " Not found"));
    }
}
