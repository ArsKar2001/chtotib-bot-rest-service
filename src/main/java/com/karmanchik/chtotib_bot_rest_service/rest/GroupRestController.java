package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.exeption.ResourceNotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@RestController
@RequestMapping("/api/")
public class GroupRestController {
    private final JpaGroupRepository groupRepository;

    public GroupRestController(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable(name = "id") @Valid Integer groupId) {
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(groupId, Group.class));
        return ResponseEntity.ok().body(group);
    }

    @PostMapping("/groups")
    public Group createGroup(@Valid @RequestBody Group group) {
        return groupRepository.save(group);
    }

    @PutMapping("/group/{id}")
    public ResponseEntity<Group> updateGroup(
            @PathVariable(name = "id") Integer groupId,
            @Valid @RequestBody Group groupDetails) {
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(groupId, Group.class));
        group.setLessons(groupDetails.getLessons());
        group.setGroupName(groupDetails.getGroupName());

        final Group upGroup = groupRepository.save(group);
        return ResponseEntity.ok().body(upGroup);
    }

    @DeleteMapping("/group/{id}")
    public Map<String, Boolean> deleteGroup(
            @PathVariable(name = "id") Integer groupId) {
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(groupId, Group.class));
        groupRepository.delete(group);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
