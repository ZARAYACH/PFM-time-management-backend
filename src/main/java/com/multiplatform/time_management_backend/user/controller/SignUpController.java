package com.multiplatform.time_management_backend.user.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.user.UserMapper;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@Tag(name = "signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    private UserDto signUp(@RequestBody UserDto.PostUserDto userDto) throws BadArgumentException {
        return userMapper.toUserDto(userService.create(userDto));
    }
}
