package com.multiplatform.time_management_backend.user.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.model.Student;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import com.multiplatform.time_management_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator = EmailValidator.getInstance();

    public List<User> list() {
        return userRepository.findAll();
    }

    public User findById(long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }
    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public User create(UserDto.PostUserDto postUserDto) throws BadArgumentException {
        validateUserDto(postUserDto);
        return userRepository.save(new Student(postUserDto.email(),
                passwordEncoder.encode(postUserDto.password()),
                postUserDto.firstName(),
                postUserDto.lastName(),
                postUserDto.birthDate()));

    }

    private void validateUserDto(UserDto userDto) throws BadArgumentException {
        try {
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }
    }

    private void validateUserDto(UserDto.PostUserDto postUserDto) throws BadArgumentException {
        try {
            validateAuthCredentials(postUserDto.email(), postUserDto.password());
        } catch (IllegalArgumentException | BadArgumentException e) {
            throw new BadArgumentException(e);
        }
    }

    private void validateAuthCredentials(String email, String password) throws BadArgumentException {
        try {
            Assert.hasText(email, "Email is required");
            Assert.isTrue(emailValidator.isValid(email), "Email is not valid");
            Assert.isTrue(!userRepository.existsByEmail(email), "Email Already Exists");
            Assert.isTrue(passwordValidator.validate(new PasswordData(password)).isValid(), "Password should least be 8 character with one uppercase, one special and one digit characters");
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
    }
}
