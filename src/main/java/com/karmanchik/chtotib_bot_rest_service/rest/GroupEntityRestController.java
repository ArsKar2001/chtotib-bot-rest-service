package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class GroupEntityRestController implements EntityRestControllerInterface<Group> {
    private final GroupService groupService;

    public GroupEntityRestController(GroupService groupService) {
        this.groupService = groupService;
    }


    @Override
    @GetMapping("/groups?id={id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            final Group group = groupService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
            return ResponseEntity.ok(group);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Override
    @GetMapping("/groups/all")
    public ResponseEntity<?> getAll() {
        final List<Group> all = groupService.findAll();
        return ResponseEntity.ok(all);
    }

    @Override
    public ResponseEntity<?> post(Group s) {
        groupService.save(s);
        return ResponseEntity.ok(s);
    }

    @Override
    @PutMapping("/groups?id={id}&group={group}")
    public <S extends Group> ResponseEntity<?> put(@PathVariable("id") @NotNull Integer id,
                                                   @PathVariable("group") @Valid @NotNull S s) {
        try {
            Group group = groupService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
            group.setLessons(s.getLessons());
            group.setReplacements(s.getReplacements());
            return ResponseEntity.ok(groupService.save(group));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    @Override
    @DeleteMapping("/groups?id={id}")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Override
    @DeleteMapping("/groups?group={group}")
    public <S extends Group> ResponseEntity<?> delete(@PathVariable("group") @Valid S s) {
        log.info("Delete group {}", s.getId());
        groupService.delete(s);
        return ResponseEntity.ok(s);
    }

    @Override
    @DeleteMapping("/groups/all")
    public ResponseEntity<?> deleteAll() {
        log.info("Deleted all groups");
        groupService.deleteAll();
        return ResponseEntity.ok("Deleted all groups");
    }
}
