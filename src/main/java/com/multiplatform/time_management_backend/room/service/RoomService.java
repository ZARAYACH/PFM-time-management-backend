package com.multiplatform.time_management_backend.room.service;

import com.multiplatform.time_management_backend.department.repository.DepartmentRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.model.Room;
import com.multiplatform.time_management_backend.room.model.dto.RoomDto;
import com.multiplatform.time_management_backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    public List<Room> list() {
        return roomRepository.findAll();
    }

    public Room findById(long id) throws NotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room with id " + id + " not found"));
    }

    public List<Room> findById(Set<Long> ids) {
        return roomRepository.findAllById(ids);
    }

    public Room create(RoomDto roomDto) throws NotFoundException, BadArgumentException {
        Room room = validateRoomDtoAndCreate(roomDto);
        return roomRepository.save(room);
    }


    public Room modify(Room room, RoomDto roomDto) throws NotFoundException, BadArgumentException {
        Room newRoom = validateRoomDtoAndCreate(roomDto);
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

    public Room validateRoomDtoAndCreate(RoomDto roomDto) throws NotFoundException, BadArgumentException {
        Room room = new Room(null, roomDto.name(), roomDto.number(), roomDto.capacity(), roomDto.emphie(), null);
        try {
            Assert.hasText(roomDto.number(), "Room number cannot be null");
            Assert.isTrue(roomDto.capacity() > 0, "Room capacity cannot less than 0");
            if (roomDto.departmentId() != null) {
                room.setDepartment(departmentRepository.findById(roomDto.departmentId())
                        .orElseThrow(() -> new NotFoundException("Department not found")));
            }
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }

        return room;
    }

}
