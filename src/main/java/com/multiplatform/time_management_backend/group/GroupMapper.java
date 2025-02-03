package com.multiplatform.time_management_backend.group;


import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.group.model.dto.GroupDto;
import com.multiplatform.time_management_backend.user.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface GroupMapper {
    @Mapping(source = "students" , target = "studentIds")
    GroupDto toGroupDto(Group group);

    List<GroupDto> toGroupDto(List<Group> groups);

    default Long map(Student student){
        return student.getId();
    };
}
