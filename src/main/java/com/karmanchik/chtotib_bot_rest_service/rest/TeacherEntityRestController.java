package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/")
public class TeacherEntityRestController implements EntityRestControllerInterface<Teacher> {
    private final TeacherService teacherService;
    private final LessonService lessonService;

    public TeacherEntityRestController(TeacherService teacherService, LessonService lessonService) {
        this.teacherService = teacherService;
        this.lessonService = lessonService;
    }

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            final Teacher teacher = teacherService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable("id") @NotNull Integer id) {
        try {

            return ResponseEntity.ok()
                    .body(teacherService.getLessonsByGroupId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> postLessons(@PathVariable("id") @NotNull Integer id,
                                         @RequestBody @Valid List<Lesson> lessons) {
        try {
            Teacher teacher = teacherService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
            return ResponseEntity.ok()
                    .body(lessonService.saveAll(lessons.stream()
                            .peek(lesson -> lesson.setTeacher(teacher))
                            .collect(Collectors.toList())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> putLessons(@PathVariable("id") @NotNull Integer id,
                                         @RequestBody @Valid List<Lesson> lessons) {
        try {
            return ResponseEntity.ok()
                    .body(lessonService.saveAll(lessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/teachers/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable("id") @NotNull Integer id) {
        try {
            return ResponseEntity.ok()
                    .body(teacherService.getReplacementsByGroupId(id));
        } catch (Exception e) {
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
        try {
            return ResponseEntity.ok()
                    .body(teacherService.save(teacherService.findById(id)
                            .map(teacher -> {
                                teacher.setName(t.getName());
                                return teacher;
                            }).orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
