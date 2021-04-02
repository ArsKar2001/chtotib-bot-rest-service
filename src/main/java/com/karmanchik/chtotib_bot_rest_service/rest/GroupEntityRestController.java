package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class GroupEntityRestController implements EntityRestControllerInterface<Group> {
    private final GroupService groupService;
    private final LessonService lessonService;

    public GroupEntityRestController(GroupService groupService, LessonService lessonService) {
        this.groupService = groupService;
        this.lessonService = lessonService;
    }


    @Override
    @GetMapping("/groups/{id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            return ResponseEntity.ok()
                    .body(groupService.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(id, Group.class)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/groups/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable("id") @NotNull Integer id) {
        try {
            return ResponseEntity.ok()
                    .body(groupService.getLessonsByGroupId(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/groups/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable("id") @NotNull Integer id) {
        try {
            return ResponseEntity.ok()
                    .body(groupService.getReplacementByGroupId(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @Override
    @GetMapping("/groups")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(groupService.findAll());
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
