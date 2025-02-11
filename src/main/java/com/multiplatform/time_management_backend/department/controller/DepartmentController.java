package com.multiplatform.time_management_backend.department.controller;

import com.multiplatform.time_management_backend.department.DepartmentMapper;
import com.multiplatform.time_management_backend.department.model.Department;
import com.multiplatform.time_management_backend.department.model.dto.DepartmentDto;
import com.multiplatform.time_management_backend.department.service.DepartmentService;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Department")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DepartmentService departmentService;

    @GetMapping
    private List<DepartmentDto> listDepartment() {
        return departmentMapper.toDepartmentDto(departmentService.list());
    }

    @GetMapping("/{id}")
    private DepartmentDto findDepartmentById(@PathVariable long id) throws NotFoundException {
        return departmentMapper.toDepartmentDto(departmentService.findById(id));
    }


    @PostMapping
    private DepartmentDto createDepartment(@RequestBody DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        return departmentMapper.toDepartmentDto(departmentService.create(departmentDto));
    }

    @PutMapping("/{id}")
    private DepartmentDto modifyDepartment(@PathVariable long id, @RequestBody DepartmentDto departmentDto) throws NotFoundException, BadArgumentException {
        Department department = departmentService.findById(id);
        return departmentMapper.toDepartmentDto(departmentService.modify(department, departmentDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteDepartment(@PathVariable long id) throws NotFoundException {
        Department department = departmentService.findById(id);
        departmentService.delete(department);
        return Collections.singletonMap("deleted", true);

    }

}
