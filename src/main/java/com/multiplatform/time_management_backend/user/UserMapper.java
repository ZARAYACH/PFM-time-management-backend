package com.multiplatform.time_management_backend.user;

import com.multiplatform.time_management_backend.user.model.Student;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.model.dto.TeacherDto;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(source = "group.name", target = "groupName")
    UserDto toUserDto(Student student);

    List<UserDto> toUser(List<User> users);

    List<TeacherDto> toTeacherDto(List<Teacher> teachers);
}
