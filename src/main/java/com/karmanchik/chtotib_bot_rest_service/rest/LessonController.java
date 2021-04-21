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
    @PostMapping("/lessons")
    public ResponseEntity<?> post(@RequestBody @Valid Lesson lesson) {
        LessonModel model = assembler.toModel(lessonsRepository.save(lesson));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Lesson lesson) {
        LessonModel model = lessonsRepository.findById(id)
                .map(l -> {
                    l.setDay(lesson.getDay());
                    l.setGroup(lesson.getGroup());
                    l.setTeachers(lesson.getTeachers());
                    l.setDiscipline(lesson.getDiscipline());
                    l.setPairNumber(lesson.getPairNumber());
                    l.setWeekType(lesson.getWeekType());
                    l.setAuditorium(lesson.getAuditorium());
                    return lessonsRepository.save(l);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        lessonsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(lessonsRepository::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/lessons/")
    public ResponseEntity<?> deleteAll() {
        lessonsRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
