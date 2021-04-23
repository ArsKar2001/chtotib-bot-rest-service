package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.LessonAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaLessonsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class LessonController implements Controller<Lesson> {
    private final JpaLessonsRepository lessonsRepository;
    private final LessonAssembler assembler;


    @Override
    @GetMapping("/lessons/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        LessonModel model = lessonsRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/lessons")
    public ResponseEntity<?> getAllByDay(@RequestParam @NotNull Integer day) {
        List<Lesson> lessons = lessonsRepository.findAllByDayOrderByPairNumber(day);
        CollectionModel<LessonModel> models = assembler.toCollectionModel(lessons)
                .add(linkTo(methodOn(LessonController.class).getAllByDay(day)).withRel("day_" + day));
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @GetMapping("/lessons/")
    public ResponseEntity<?> getAll() {
        List<Lesson> lessons = lessonsRepository.findAll();
        CollectionModel<LessonModel> models = assembler.toCollectionModel(lessons);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping(value = "/lesson")
    public ResponseEntity<?> post(@RequestBody @Valid Lesson lesson) {
        LessonModel model = assembler.toModel(lessonsRepository.save(lesson));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PostMapping(value = "/lessons")
    public ResponseEntity<?> postArray(@RequestBody @Valid List<Lesson> lessons) {
        CollectionModel<LessonModel> models = assembler.toCollectionModel(lessonsRepository.saveAll(lessons));
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PutMapping(value = "/lessons/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Lesson lesson) {
        LessonModel model = lessonsRepository.findById(id)
                .map(l -> changeLesson(lesson, l)).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/lessons")
    public ResponseEntity<?> putArray(@RequestBody @Valid List<Lesson> lessons) {
        List<Lesson> lessonList = lessons.stream()
                .map(lesson -> lessonsRepository.findById(lesson.getId())
                        .map(l -> changeLesson(lesson, l))
                        .orElseThrow(() -> new ResourceNotFoundException(lesson.getId(), Lesson.class)))
                .collect(Collectors.toList());
        CollectionModel<LessonModel> models = assembler.toCollectionModel(lessonList);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @DeleteMapping("/lesson")
    public ResponseEntity<?> delete(@RequestParam("id") @NotNull Integer id) {
        lessonsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(id -> {
            lessonsRepository.deleteById(id);
            log.info("Deleted lesson by id = {}", id);
        });
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/lessons/")
    public ResponseEntity<?> deleteAll() {
        lessonsRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private Lesson changeLesson(@RequestBody @Valid Lesson oldLesson, Lesson newLesson) {
        newLesson.setDay(oldLesson.getDay());
        newLesson.setGroup(oldLesson.getGroup());
        newLesson.setTeachers(oldLesson.getTeachers());
        newLesson.setDiscipline(oldLesson.getDiscipline());
        newLesson.setPairNumber(oldLesson.getPairNumber());
        newLesson.setWeekType(oldLesson.getWeekType());
        newLesson.setAuditorium(oldLesson.getAuditorium());
        return lessonsRepository.save(newLesson);
    }
}
