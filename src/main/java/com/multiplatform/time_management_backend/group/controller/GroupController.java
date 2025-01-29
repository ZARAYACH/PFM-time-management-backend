package com.multiplatform.time_management_backend.group.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.GroupMapper;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.group.model.dto.GroupDto;
import com.multiplatform.time_management_backend.group.service.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "Group")
public class GroupController {

    private final GroupMapper groupMapper;
    private final GroupService groupService;

    @GetMapping
    private List<GroupDto> list() {
        return groupMapper.toGroupDto(groupService.list());
    }

    @GetMapping("/{id}")
    private GroupDto findById(@PathVariable long id) throws NotFoundException {
        return groupMapper.toGroupDto(groupService.findById(id));
    }

    @GetMapping("/{ids}")
    private List<GroupDto> findByIds(@PathVariable Set<Long> ids) {
        return groupMapper.toGroupDto(groupService.findById(ids));
    }

    @PostMapping
    private GroupDto create(@RequestBody GroupDto groupDto) throws NotFoundException, BadArgumentException {
        return groupMapper.toGroupDto(groupService.create(groupDto));
    }

    @PostMapping("/{id}")
    private GroupDto modify(@PathVariable long id, @RequestBody GroupDto groupDto) throws NotFoundException, BadArgumentException {
        Group group = groupService.findById(id);
        return groupMapper.toGroupDto(groupService.modify(group, groupDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> delete(@PathVariable long id) throws NotFoundException {
        Group group = groupService.findById(id);
        groupService.delete(group);
        return Collections.singletonMap("deleted", true);
    }

}
