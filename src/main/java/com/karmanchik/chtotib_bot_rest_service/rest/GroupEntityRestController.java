package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/groups/")
public class GroupEntityRestController implements EntityRestControllerInterface<Group> {
    private final GroupService groupService;

    public GroupEntityRestController(GroupService groupService) {
        this.groupService = groupService;
    }


    @Override
    @GetMapping("/{id}")
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
    @GetMapping("/all")
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
    public <S extends Group> ResponseEntity<?> put(@NotNull Integer id, @Valid @NotNull S s) {
        return null;
    }

    @Override
    public ResponseEntity<?> put(Group group) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    public <S extends Group> ResponseEntity<?> delete(S s) {
        log.info("Delete group {}", s.getId());
        groupService.delete(s);
        return ResponseEntity.ok(s);
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        log.info("Deleted all groups");
        groupService.deleteAll();
        return ResponseEntity.ok("Deleted all groups");
    }
}
