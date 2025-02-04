package com.multiplatform.time_management_backend.room.service;

import com.multiplatform.time_management_backend.department.repository.DepartmentRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.model.dto.ClassRoomDto;
import com.multiplatform.time_management_backend.room.repository.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final DepartmentRepository departmentRepository;

    public List<ClassRoom> list() {
        return classRoomRepository.findAll();
    }

    public ClassRoom findById(long id) throws NotFoundException {
        return classRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("ClassRoom with id " + id + " not found"));
    }

    public List<ClassRoom> findById(Set<Long> ids) {
        return classRoomRepository.findAllById(ids);
    }

    public ClassRoom create(ClassRoomDto classRoomDto) throws NotFoundException, BadArgumentException {
        ClassRoom classRoom = validateRoomDtoAndCreate(classRoomDto);
        return classRoomRepository.save(classRoom);
    }


    public ClassRoom modify(ClassRoom classRoom, ClassRoomDto classRoomDto) throws NotFoundException, BadArgumentException {
        ClassRoom newClassRoom = validateRoomDtoAndCreate(classRoomDto);
        classRoom.setName(newClassRoom.getName());
        classRoom.setCapacity(newClassRoom.getCapacity());
        classRoom.setNumber(newClassRoom.getNumber());
        classRoom.setAmphie(newClassRoom.isAmphie());
        classRoom.setDepartment(newClassRoom.getDepartment());
        return classRoomRepository.save(classRoom);
    }

    public void delete(ClassRoom classRoom) {
        classRoom.setDepartment(null);
        classRoomRepository.delete(classRoom);
    }

    public ClassRoom validateRoomDtoAndCreate(ClassRoomDto classRoomDto) throws NotFoundException, BadArgumentException {
        ClassRoom classRoom = new ClassRoom(null, classRoomDto.name(), classRoomDto.number(), classRoomDto.type(), classRoomDto.capacity(), classRoomDto.amphie(), null);
        try {
            Assert.hasText(classRoomDto.number(), "ClassRoom number cannot be null");
            Assert.isTrue(classRoomDto.capacity() > 0, "ClassRoom capacity cannot less than 0");
            if (classRoomDto.departmentId() != null) {
                classRoom.setDepartment(departmentRepository.findById(classRoomDto.departmentId())
                        .orElseThrow(() -> new NotFoundException("Department not found")));
            }
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }

        return classRoom;
    }

    public List<ClassRoom> findByType(ClassRoom.Type classRoomType) {
        return classRoomRepository.findAllByType(classRoomType);
    }
}
