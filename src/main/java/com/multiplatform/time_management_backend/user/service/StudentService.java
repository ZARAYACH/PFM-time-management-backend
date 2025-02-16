package com.multiplatform.time_management_backend.user.service;

import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.model.Student;
import com.multiplatform.time_management_backend.user.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student findByEmail(long id) throws NotFoundException {
        return studentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Teacher with id" + id + " Not found"));
    }
    public Student findByEmail(String email) throws NotFoundException {
        return studentRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Teacher with email" + email + " Not found"));
    }

}
