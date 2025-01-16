package com.multiplatform.time_management_backend.user.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.UserMapper;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    private List<UserDto> list() {
        return userMapper.toUser(userService.list());
    }

    @GetMapping("/{id}")
    private UserDto getById(@PathVariable long id) throws NotFoundException {
        return userMapper.toUserDto(userService.findById(id));
    }

    @PostMapping
    private UserDto create(@RequestBody UserDto.PostUserDto userDto) throws BadArgumentException {
        return userMapper.toUserDto(userService.create(userDto));
    }
}
