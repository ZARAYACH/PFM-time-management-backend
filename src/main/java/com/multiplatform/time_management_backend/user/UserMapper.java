package com.multiplatform.time_management_backend.user;

import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUser(List<User> users);
}
