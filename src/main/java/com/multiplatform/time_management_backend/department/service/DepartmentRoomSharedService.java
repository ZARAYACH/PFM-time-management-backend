package com.multiplatform.time_management_backend.department.service;

import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.department.model.dto.DepartmentDto;
import com.multiplatform.time_management_backend.department.repository.DepartmentRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.model.Room;
import com.multiplatform.time_management_backend.room.model.dto.RoomDto;
import com.multiplatform.time_management_backend.room.repository.RoomRepository;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentRoomSharedService {

    private final TeacherService teacherService;
    private final RoomRepository roomRepository;
    private final DepartmentRepository departmentRepository;

    public Department validateDepartmentDtoAndCreate(DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        try {
            Assert.notNull(departmentDto.name(), "Department name cannot be null");
            Assert.notNull(departmentDto.chiefId(), "Department chief cannot be null");
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        Teacher teacher = teacherService.findById(departmentDto.chiefId());
        Department department = new Department(null, departmentDto.name(), teacher, null);
        if (departmentDto.roomIds() != null && !departmentDto.roomIds().isEmpty()) {
            department.setRooms(findRoomById(departmentDto.roomIds()));
        }
        return department;
    }

    public Room validateRoomDtoAndCreate(RoomDto roomDto) throws NotFoundException, BadArgumentException {
        try {
            Assert.hasText(roomDto.number(), "Room number cannot be null");
            Assert.isTrue(roomDto.capacity() > 0, "Room capacity cannot less than 0");
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        Room room = new Room(null, roomDto.name(), roomDto.number(), roomDto.capacity(), roomDto.emphie(), null);
        if (roomDto.departmentId() != null) {
            room.setDepartment(findDepartmentById(roomDto.departmentId()));
        }
        return room;
    }

    public Room findRoomById(long id) throws NotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room with id " + id + " not found"));
    }

    public List<Room> findRoomById(Set<Long> ids) {
        return roomRepository.findAllById(ids);
    }

    public Department findDepartmentById(long id) throws NotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department with id " + id + " not found"));
    }

    public List<Department> findDepartmentById(Set<Long> ids) {
        return departmentRepository.findAllById(ids);
    }
}
