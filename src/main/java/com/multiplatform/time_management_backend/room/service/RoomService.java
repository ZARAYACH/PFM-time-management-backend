package com.multiplatform.time_management_backend.room.service;

import com.multiplatform.time_management_backend.department.service.DepartmentRoomSharedService;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.model.Room;
import com.multiplatform.time_management_backend.room.model.dto.RoomDto;
import com.multiplatform.time_management_backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRoomSharedService departmentRoomSharedService;

    public List<Room> list() {
        return roomRepository.findAll();
    }

    public Room findById(long id) throws NotFoundException {
        return departmentRoomSharedService.findRoomById(id);
    }

    public List<Room> findById(Set<Long> ids) {
        return departmentRoomSharedService.findRoomById(ids);
    }


    public Room create(RoomDto roomDto) throws NotFoundException {
        Room room = departmentRoomSharedService.validateRoomDtoAndCreate(roomDto);
        return roomRepository.save(room);
    }


    public Room modify(Room room, RoomDto roomDto) throws NotFoundException {
        Room newRoom = departmentRoomSharedService.validateRoomDtoAndCreate(roomDto);
        room.setName(newRoom.getName());
        room.setCapacity(newRoom.getCapacity());
        room.setNumber(newRoom.getNumber());
        room.setEmphie(newRoom.isEmphie());
        room.setDepartment(newRoom.getDepartment());
        return roomRepository.save(room);
    }

    public void delete(Room room) {
        room.setDepartment(null);
        roomRepository.delete(room);
    }
}
