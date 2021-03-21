package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class GroupRestController {
    private final GroupService groupService;

    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<Group> getAllGroups() {
        return groupService.findAll();
    }

    @GetMapping("/group/{id}")
    @ResponseBody
    public ResponseEntity<Object> getGroup(@PathVariable(name = "id") @Valid Integer groupId) {
        try {
            Group group = groupService
                    .findById(groupId);
            return ResponseEntity.ok().body(group);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/groups")
    @ResponseBody
    public Group createGroup(@Valid @RequestBody Group group) {
        return groupService.save(group);
    }

    @PutMapping("/group/{id}")
    @ResponseBody
    public ResponseEntity<Object> updateGroup(
            @PathVariable(name = "id") Integer groupId,
            @Valid @RequestBody Group groupDetails) {
        try {
            Group group = groupService.findById(groupId);
            group.setLessons(groupDetails.getLessons());
            group.setGroupName(groupDetails.getGroupName());

            final Group upGroup = groupService.save(group);
            log.debug("Put group: {}", group);
            return ResponseEntity.ok().body(upGroup);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/group/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteGroup(
            @PathVariable(name = "id") Integer groupId) {
        try {
            Group group = groupService.findById(groupId);
            groupService.delete(group);
            log.debug("Группа {} удалена", group.getGroupName());
            return ResponseEntity.ok().body("Группа " + group.getGroupName() + " удалена");
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
