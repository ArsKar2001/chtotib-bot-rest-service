package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.LessonModelAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.ReplacementModelAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.TeacherModelAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/")
public class TeacherController extends BaseController<Teacher, TeacherService> {
    private final TeacherService teacherService;
    private final TeacherModelAssembler assembler;
    private final LessonModelAssembler lessonModelAssembler;
    private final ReplacementModelAssembler replacementModelAssembler;

    public TeacherController(TeacherService teacherService,
                             TeacherModelAssembler assembler,
                             LessonModelAssembler lessonModelAssembler,
                             ReplacementModelAssembler replacementModelAssembler) {
        super(teacherService);
        this.teacherService = teacherService;
        this.assembler = assembler;
        this.lessonModelAssembler = lessonModelAssembler;
        this.replacementModelAssembler = replacementModelAssembler;
    }

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        TeacherModel model = teacherService.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Lesson> lessons = teacherService.getLessonsByTeacherId(id);
        List<List<Lesson>> sortLessons = new ArrayList<>();
        lessons.stream()
                .map(Lesson::getDay)
                .sorted()
                .distinct()
                .forEach(day -> {
                    sortLessons.add(lessons.stream()
                            .filter(lesson -> lesson.getDay().equals(day))
                            .sorted(Comparator.comparing(Lesson::getPairNumber))
                            .collect(Collectors.toList()));
                });
        List<CollectionModel<LessonModel>> models = sortLessons.stream()
                .map(lessonModelAssembler::toCollectionModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @GetMapping("/teachers/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Replacement> replacements = teacherService.getReplacementsByTeacherId(id);
        List<List<Replacement>> sortReplacement = new ArrayList<>();
        replacements.stream()
                .map(Replacement::getDate)
                .sorted()
                .distinct()
                .forEach(day -> {
                    sortReplacement.add(replacements.stream()
                            .filter(replacement -> replacement.getDate().equals(day))
                            .sorted(Comparator.comparing(Replacement::getPairNumber))
                            .collect(Collectors.toList()));
                });
        List<CollectionModel<ReplacementModel>> models = sortReplacement.stream()
                .map(replacementModelAssembler::toCollectionModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @GetMapping("/teachers/")
    public ResponseEntity<?> getAll() {
        List<Teacher> teachers = teacherService.findAll();
        CollectionModel<TeacherModel> models = assembler.toCollectionModel(teachers);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping("/teachers")
    public ResponseEntity<?> post(@RequestBody @Valid Teacher teacher) {
        TeacherModel model = assembler.toModel(teacherService.save(teacher));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping("/teachers/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Teacher teacher) {
        TeacherModel model = teacherService.findById(id)
                .map(t -> {
                    t.setName(teacher.getName());
                    t.setLessons(teacher.getLessons());
                    t.setReplacements(teacher.getReplacements());
                    return teacherService.save(t);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/teacher")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(teacherService::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/teacher/")
    public ResponseEntity<?> deleteAll() {
        teacherService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
