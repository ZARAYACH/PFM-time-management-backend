package com.multiplatform.time_management_backend.user.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.UserMapper;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    private List<UserDto> listUsers() {
        return userMapper.toUser(userService.list());
    }

    @GetMapping("/{id}")
    private UserDto getUserById(@PathVariable long id) throws NotFoundException {
        return userMapper.toUserDto(userService.findById(id));
    }

    @PostMapping
    private UserDto createUser(@RequestBody UserDto.PostUserDto userDto) throws BadArgumentException {
        return userMapper.toUserDto(userService.createWithRole(userDto));
    }

    @PutMapping("/{id}")
    private UserDto modifyUser(@RequestBody UserDto.PostUserDto userDto, @PathVariable Long id) throws NotFoundException, BadArgumentException {
        User user = userService.findById(id);
        return userMapper.toUserDto(userService.modify(user, userDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteUser(@PathVariable Long id) throws NotFoundException {
        User user = userService.findById(id);
        userService.delete(user);
        return Collections.singletonMap("deleted", true);
    }
}
