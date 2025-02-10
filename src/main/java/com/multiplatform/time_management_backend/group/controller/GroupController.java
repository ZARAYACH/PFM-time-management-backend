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
    private List<GroupDto> listGroup() {
        return groupMapper.toGroupDto(groupService.list());
    }

    @GetMapping("/{id}")
    private GroupDto findGroupById(@PathVariable long id) throws NotFoundException {
        return groupMapper.toGroupDto(groupService.findById(id));
    }

    @PostMapping
    private GroupDto createGroup(@RequestBody GroupDto groupDto) throws NotFoundException, BadArgumentException {
        return groupMapper.toGroupDto(groupService.create(groupDto));
    }

    @PutMapping("/{id}")
    private GroupDto modifyGroup(@PathVariable long id, @RequestBody GroupDto groupDto) throws NotFoundException, BadArgumentException {
        Group group = groupService.findById(id);
        return groupMapper.toGroupDto(groupService.modify(group, groupDto));
    }

    @DeleteMapping("/{id}")
    private Map<String, Boolean> deleteGroup(@PathVariable long id) throws NotFoundException {
        Group group = groupService.findById(id);
        groupService.delete(group);
        return Collections.singletonMap("deleted", true);
    }

}
