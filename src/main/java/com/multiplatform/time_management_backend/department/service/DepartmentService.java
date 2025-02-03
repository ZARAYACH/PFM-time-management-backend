package com.multiplatform.time_management_backend.department.service;

import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.department.model.dto.DepartmentDto;
import com.multiplatform.time_management_backend.department.repository.DepartmentRepository;
import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.repository.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final TeacherRepository teacherRepository;

    public List<Department> list() {
        return departmentRepository.findAll();
    }

    public Department findById(long id) throws NotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department with id " + id + " not found"));
    }

    public List<Department> findById(Set<Long> ids) {
        return departmentRepository.findAllById(ids);
    }

    public Department create(DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department department = validateDepartmentDtoAndCreate(departmentDto);
        return departmentRepository.save(department);
    }

    public Department modify(Department department, DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department newDepartment = validateDepartmentDtoAndCreate(departmentDto);
        department.setName(newDepartment.getName());
        department.setChief(newDepartment.getChief());
        department.setClassRooms(newDepartment.getClassRooms());
        return departmentRepository.save(department);
    }

    public void delete(Department department) {
        department.setChief(null);
        department.setClassRooms(null);
        departmentRepository.delete(department);
    }

    public Department validateDepartmentDtoAndCreate(DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department department = new Department(null, departmentDto.name(), null, null);
        try {
            Assert.notNull(departmentDto.name(), "Department name cannot be null");
            Assert.notNull(departmentDto.chiefId(), "Department chief cannot be null");
            department.setChief(teacherRepository.findById(departmentDto.chiefId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found")));
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        if (departmentDto.roomIds() != null && !departmentDto.roomIds().isEmpty()) {
            List<ClassRoom> classRooms = classRoomRepository.findAllById(departmentDto.roomIds());
            classRooms.forEach(classRoom -> classRoom.setDepartment(department));
            department.setClassRooms(classRooms);
        }
        return department;
    }


}
