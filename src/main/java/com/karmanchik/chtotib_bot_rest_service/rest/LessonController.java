package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import com.karmanchik.chtotib_bot_rest_service.model.LessonList;
import com.karmanchik.chtotib_bot_rest_service.rest.assembler.ModelAssembler;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
public class LessonController implements Controller<Lesson> {
    private final ModelAssembler<Lesson> assembler;
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
        assembler = new ModelAssembler<>(this.getClass());
    }

    /**
     * Возвращает один элемент из таблицы БД
     *
     * @param id индентификатор эелемента
     * @return елемент
     */
    @Override
    @GetMapping("/lessons/{id}")
    public EntityModel<Lesson> get(@PathVariable @NotNull Integer id) {
        Lesson lesson = lessonService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        return assembler.toModel(lesson);
    }

    /**
     * Вернет коллекцию всех элементов из таблицы БД
     *
     * @return коллекция элементов
     */
    @Override
    @GetMapping("/lessons")
    public CollectionModel<EntityModel<Lesson>> getAll() {
        List<EntityModel<Lesson>> lessons = lessonService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(lessons,
                linkTo(methodOn(LessonController.class).getAll()).withSelfRel());
    }

    /**
     * Добавит новый элемент в таблицу БД
     *
     * @param lesson тело элемент
     * @return сохраненный элемент
     */
    @Override
    @PostMapping("/lessons")
    public ResponseEntity<?> post(@RequestBody @Valid Lesson lesson) {
        EntityModel<Lesson> model = assembler.toModel(lessonService.save(lesson));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PostMapping("/lessons")
    public ResponseEntity<?> postAll(@RequestBody @Valid LessonList lessons) {
        List<Lesson> lessonList = lessonService.saveAll(lessons.getLessons());
        List<EntityModel<Lesson>> models = lessonList.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Lesson>> model = CollectionModel.of(models,
                linkTo(methodOn(LessonController.class).getAll()).withSelfRel());
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    /**
     * Изменит элемент таблицы БД
     *
     * @param id     индентификатор элемента БД
     * @param lesson тело элемента
     * @return измененный элемент
     */
    @Override
    @PutMapping("/lessons/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Lesson lesson) {
        Lesson les = lessonService.findById(id)
                .map(l -> {
                    l.setAuditorium(lesson.getAuditorium());
                    l.setDay(lesson.getDay());
                    l.setDiscipline(lesson.getDiscipline());
                    l.setPairNumber(lesson.getPairNumber());
                    l.setWeekType(lesson.getWeekType());
                    l.setGroup(lesson.getGroup());
                    l.setTeacher(lesson.getTeacher());
                    return lessonService.save(l);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
        EntityModel<Lesson> model = assembler.toModel(les);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/lessons")
    public ResponseEntity<?> putAll(@RequestBody() @Valid LessonList lessons) {
        List<Lesson> lessonList = lessons.getLessons().stream()
                .map(lesson -> lessonService.findById(lesson.getId())
                        .map(l -> {
                            l.setAuditorium(lesson.getAuditorium());
                            l.setDay(lesson.getDay());
                            l.setDiscipline(lesson.getDiscipline());
                            l.setPairNumber(lesson.getPairNumber());
                            l.setWeekType(lesson.getWeekType());
                            l.setGroup(lesson.getGroup());
                            l.setTeacher(lesson.getTeacher());
                            return lessonService.save(l);
                        }).orElseThrow(() -> new ResourceNotFoundException(lesson.getId(), Lesson.class)))
                .collect(Collectors.toList());
        List<EntityModel<Lesson>> models = lessonList.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Lesson>> model = CollectionModel.of(models,
                linkTo(methodOn(LessonController.class).getAll()).withSelfRel());
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    /**
     * Удалит элемент из таблицы БД
     *
     * @param id индентификатор элемента таблицы БД
     * @return
     */
    @Override
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        lessonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удалит все элементы из таблицы БД
     *
     * @return
     */
    @Override
    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll() {
        lessonService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lessons")
    public ResponseEntity<?> deleteAll(@RequestBody LessonList lessons) {
        lessonService.deleteAll(lessons.getLessons());
        return ResponseEntity.noContent().build();
    }
}
