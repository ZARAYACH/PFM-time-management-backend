package com.multiplatform.time_management_backend.department.service;

import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.department.model.dto.DepartmentDto;
import com.multiplatform.time_management_backend.department.repository.DepartmentRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.service.RoomService;
import com.multiplatform.time_management_backend.user.model.Teacher;
import com.multiplatform.time_management_backend.user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentRoomSharedService departmentRoomSharedService;

    public List<Department> list() {
        return departmentRepository.findAll();
    }

    public Department findById(long id) throws NotFoundException {
        return departmentRoomSharedService.findDepartmentById(id);
    }

    public List<Department> findById(Set<Long> ids) {
        return departmentRoomSharedService.findDepartmentById(ids);
    }

    public Department create(DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department department = departmentRoomSharedService.validateDepartmentDtoAndCreate(departmentDto);
        return departmentRepository.save(department);
    }

    public Department modify(Department department, DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department newDepartment = departmentRoomSharedService.validateDepartmentDtoAndCreate(departmentDto);
        department.setName(newDepartment.getName());
        department.setChief(newDepartment.getChief());
        department.setRooms(newDepartment.getRooms());
        return departmentRepository.save(department);
    }

    public void delete(Department department) {
        department.setChief(null);
        department.setRooms(null);
        departmentRepository.delete(department);
    }

}
