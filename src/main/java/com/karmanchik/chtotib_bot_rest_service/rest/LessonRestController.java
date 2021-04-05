package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class LessonRestController implements EntityRestControllerInterface<Lesson> {
    private final LessonService lessonService;

    @Override
    @GetMapping("/lessons/{id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            return ResponseEntity.ok()
                    .body(lessonService.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    @GetMapping("/lessons")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @Override
    @PostMapping("/lessons")
    public ResponseEntity<?> post(@RequestBody @Valid Lesson lesson) {
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @PostMapping("/lessons")
    public ResponseEntity<?> post(@RequestBody @Valid List<Lesson> lesson) {
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @Override
    @PutMapping("/lessons/{id}")
    public ResponseEntity<?> put(@PathVariable("id") @NotNull Integer id,
                                 @RequestBody @Valid Lesson lesson) {
        try {
            return ResponseEntity.ok()
                    .body(lessonService.findById(id)
                    .map(l -> {
                        l.setTeacher(lesson.getTeacher());
                        l.setGroup(lesson.getGroup());
                        l.setDay(lesson.getDay());
                        l.setAuditorium(lesson.getAuditorium());
                        l.setDiscipline(lesson.getDiscipline());
                        l.setPairNumber(lesson.getPairNumber());
                        l.setWeekType(lesson.getWeekType());
                        return lessonService.save(l);
                    })
                    .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @Override
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull Integer id) {
        lessonService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Override
    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll() {
        lessonService.deleteAll();
        return ResponseEntity.ok("OK");
    }
}
