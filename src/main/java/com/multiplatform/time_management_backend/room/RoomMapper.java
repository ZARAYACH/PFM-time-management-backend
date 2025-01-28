package com.multiplatform.time_management_backend.room;


import com.multiplatform.time_management_backend.room.model.Room;
import com.multiplatform.time_management_backend.room.model.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface RoomMapper {

    RoomDto toRoomDto(Room room);
    List<RoomDto> toRoomDto(List<Room> rooms);

}
