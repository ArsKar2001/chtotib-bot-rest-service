package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class LessonController implements Controller<Lesson> {
    public ResponseEntity<?> get(@NotNull Integer id) {
        return null;
    }

    public ResponseEntity<?> getAll() {
        return null;
    }

    public ResponseEntity<?> post(Lesson lesson) {
        return null;
    }

    public ResponseEntity<?> put(@NotNull Integer id, Lesson lesson) {
        return null;
    }

    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    public ResponseEntity<?> deleteAll(List<Integer> values) {
        return null;
    }
}
