package com.multiplatform.time_management_backend.user.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.UserMapper;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.model.dto.TeacherDto;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import com.multiplatform.time_management_backend.user.service.StudentService;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    private List<TeacherDto> listTeachers() {
        return userMapper.toTeacherDto(teacherService.list());
    }

    @GetMapping("/{id}")
    private TeacherDto getTeacherById(@PathVariable long id) throws NotFoundException {
        return userMapper.toTeacherDto(teacherService.findById(id));
    }

    @PutMapping("/{id}")
    private TeacherDto modifyTeacher(@RequestBody UserDto.PostUserDto userDto, @PathVariable Long id) throws NotFoundException, BadArgumentException {
        Teacher teacher = teacherService.findById(id);
        return userMapper.toTeacherDto(teacherService.modify(teacher, userDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteTeacher(@PathVariable Long id) throws NotFoundException {
        Teacher user = teacherService.findById(id);
        userService.delete(user);
        return Collections.singletonMap("deleted", true);
    }
}
