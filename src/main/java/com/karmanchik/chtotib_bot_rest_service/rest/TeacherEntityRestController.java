package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Log4j2
@RestController
@RequestMapping("/api/v1/teachers/")
public class TeacherEntityRestController implements EntityRestControllerInterface<Teacher> {
    private final TeacherService teacherService;

    public TeacherEntityRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    @GetMapping("/{id}")
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
    public ResponseEntity<?> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> post(Teacher teacher) {
        return null;
    }

    @Override
    public <S extends Teacher> ResponseEntity<?> put(@NotNull Integer id, @Valid @NotNull S s) {
        return null;
    }

    @Override
    public <S extends Teacher> ResponseEntity<?> put(S s) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    public <S extends Teacher> ResponseEntity<?> delete(S s) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        return null;
    }
}
