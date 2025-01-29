package com.multiplatform.time_management_backend.group;


import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.group.model.dto.GroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface GroupMapper {

    GroupDto toGroupDto(Group group);

    List<GroupDto> toGroupDto(List<Group> groups);

}
