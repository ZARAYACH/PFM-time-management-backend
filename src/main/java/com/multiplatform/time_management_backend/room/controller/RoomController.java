package com.multiplatform.time_management_backend.room.controller;

import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.RoomMapper;
import com.multiplatform.time_management_backend.room.model.Room;
import com.multiplatform.time_management_backend.room.model.dto.RoomDto;
import com.multiplatform.time_management_backend.room.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room")
public class RoomController {

    private final RoomMapper roomMapper;
    private final RoomService roomService;

    @GetMapping
    private List<RoomDto> list() {
        return roomMapper.toRoomDto(roomService.list());
    }

    @GetMapping("/{id}")
    private RoomDto findById(@PathVariable long id) throws NotFoundException {
        return roomMapper.toRoomDto(roomService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<RoomDto> findByIds(@PathVariable Set<Long> ids) {
        return roomMapper.toRoomDto(roomService.findById(ids));
    }

    @PostMapping
    private RoomDto create(@RequestBody RoomDto roomDto) throws NotFoundException {
        return roomMapper.toRoomDto(roomService.create(roomDto));
    }

    @PostMapping("/{id}")
    private RoomDto modify(@PathVariable long id, @RequestBody RoomDto roomDto) throws NotFoundException {
        Room room = roomService.findById(id);
        return roomMapper.toRoomDto(roomService.modify(room, roomDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        Room room = roomService.findById(id);
        roomService.delete(room);
        return Collections.singletonMap("deleted", true);
    }

}
