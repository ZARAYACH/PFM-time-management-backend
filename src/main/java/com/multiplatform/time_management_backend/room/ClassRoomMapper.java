package com.multiplatform.time_management_backend.room;


import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.model.dto.ClassRoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface ClassRoomMapper {

    ClassRoomDto toClassRoomDto(ClassRoom classRoom);
    List<ClassRoomDto> toClassRoomDto(List<ClassRoom> classRooms);

}
