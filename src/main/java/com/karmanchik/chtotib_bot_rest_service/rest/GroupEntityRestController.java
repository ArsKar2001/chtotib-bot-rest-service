package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;


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
    public CollectionModel<EntityModel<Group>> getAll() {
        return null;
    }

    @Override
    @PostMapping(value = "/groups", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> post(Group s) {
        return ResponseEntity.ok()
                .body(groupService.save(s));
    }

    @Override
    @PutMapping(value = "/groups/{id}")
    public ResponseEntity<?> put(@NotNull @PathVariable("id") Integer id,
                                 @RequestBody @Valid Group t) {
        return ResponseEntity.ok()
                .body(groupService.findById(id)
                        .map(group -> {
                            group.setName(t.getName());
                            group.setLessons(t.getLessons());
                            group.setReplacements(t.getReplacements());
                            return group;
                        })
                        .orElseGet(() -> {
                            t.setId(id);
                            return groupService.save(t);
                        }));
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
