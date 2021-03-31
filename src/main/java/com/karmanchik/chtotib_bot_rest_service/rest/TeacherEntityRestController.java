package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class TeacherEntityRestController implements EntityRestControllerInterface<Teacher> {
    private final TeacherService teacherService;

    public TeacherEntityRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            final Teacher teacher = teacherService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
            return ResponseEntity.ok(teacher);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Override
    @GetMapping("/teachers")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok()
                .body(teacherService.findAll());
    }

    @Override
    @PostMapping(value = "/teachers/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> post(@RequestBody @Valid Teacher t) {
        return ResponseEntity.ok()
                .body(teacherService.save(t));
    }

    @Override
    @PutMapping(value = "/teachers/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> put(@PathVariable("id") @NotNull Integer id,
                                 Teacher t) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    @DeleteMapping("/teachers/all")
    public ResponseEntity<?> deleteAll() {
        return null;
    }
}
