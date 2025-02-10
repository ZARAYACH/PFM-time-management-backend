package com.multiplatform.time_management_backend.room.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.room.ClassRoomMapper;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.model.dto.ClassRoomDto;
import com.multiplatform.time_management_backend.room.service.ClassRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/class-rooms")
@RequiredArgsConstructor
@Tag(name = "ClassRoom")
public class ClassRoomController {

    private final ClassRoomMapper classRoomMapper;
    private final ClassRoomService classRoomService;

    @GetMapping
    private List<ClassRoomDto> listClassRoom() {
        return classRoomMapper.toClassRoomDto(classRoomService.list());
    }

    @GetMapping("/{id}")
    private ClassRoomDto findClassRoomById(@PathVariable long id) throws NotFoundException {
        return classRoomMapper.toClassRoomDto(classRoomService.findById(id));
    }


    @PostMapping
    private ClassRoomDto createClassRoom(@RequestBody ClassRoomDto classRoomDto) throws NotFoundException, BadArgumentException {
        return classRoomMapper.toClassRoomDto(classRoomService.create(classRoomDto));
    }

    @PutMapping("/{id}")
    private ClassRoomDto modifyClassRoom(@PathVariable long id, @RequestBody ClassRoomDto classRoomDto) throws NotFoundException, BadArgumentException {
        ClassRoom classRoom = classRoomService.findById(id);
        return classRoomMapper.toClassRoomDto(classRoomService.modify(classRoom, classRoomDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteClassRoom(@PathVariable long id) throws NotFoundException {
        ClassRoom classRoom = classRoomService.findById(id);
        classRoomService.delete(classRoom);
        return Collections.singletonMap("deleted", true);
    }

}
