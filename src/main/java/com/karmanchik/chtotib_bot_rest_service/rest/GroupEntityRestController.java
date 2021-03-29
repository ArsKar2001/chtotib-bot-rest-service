package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
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
    @GetMapping("/groups/{id}")
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
    @GetMapping("/groups")
    public ResponseEntity<?> getAll() {
        final List<Group> all = groupService.findAll();
        return ResponseEntity.ok(all);
    }

    @Override
    @PostMapping(value = "/groups", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> post(Group s) {
        groupService.save(s);
        return ResponseEntity.ok(s);
    }

    @Override
    @PutMapping(value = "/groups/{id}")
    public <S extends Group> ResponseEntity<?> put(@NotNull @PathVariable("id") Integer id,
                                                   @Valid @RequestBody S s) {
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
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Override
    @DeleteMapping("/groups/all")
    public ResponseEntity<?> deleteAll() {
        log.info("Deleted all groups");
        groupService.deleteAll();
        return ResponseEntity.ok("Deleted all groups");
    }
}
