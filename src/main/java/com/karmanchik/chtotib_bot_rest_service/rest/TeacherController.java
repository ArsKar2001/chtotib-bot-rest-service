package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.LessonAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.ReplacementAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.TeacherAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import lombok.RequiredArgsConstructor;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TeacherController implements Controller<Teacher> {
    private final JpaTeacherRepository teacherRepository;
    private final TeacherAssembler assembler;
    private final LessonAssembler lessonAssembler;
    private final ReplacementAssembler replacementAssembler;

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        TeacherModel model = teacherRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Lesson> lessons = teacherRepository.getLessonsById(id);
        List<List<Lesson>> sortLessons = new ArrayList<>();
        lessons.stream()
                .map(Lesson::getDay)
                .sorted()
                .distinct()
                .forEach(day -> sortLessons.add(lessons.stream()
                        .filter(lesson -> lesson.getDay().equals(day))
                        .sorted(Comparator.comparing(Lesson::getPairNumber))
                        .collect(Collectors.toList())));
        List<CollectionModel<LessonModel>> collect = sortLessons.stream()
                .map(lessonAssembler::toCollectionModel)
                .collect(Collectors.toList());
        CollectionModel<CollectionModel<LessonModel>> models = CollectionModel.of(collect,
                linkTo(methodOn(LessonController.class).getAll()).withRel("lessons"),
                linkTo(methodOn(GroupController.class).get(id)).withSelfRel());
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @GetMapping("/teachers/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Replacement> replacements = teacherRepository.getReplacementsById(id);
        List<List<Replacement>> sortReplacement = new ArrayList<>();
        replacements.stream()
                .map(Replacement::getDate)
                .sorted()
                .distinct()
                .forEach(day -> sortReplacement.add(replacements.stream()
                        .filter(replacement -> replacement.getDate().equals(day))
                        .sorted(Comparator.comparing(Replacement::getPairNumber))
                        .collect(Collectors.toList())));
        List<CollectionModel<ReplacementModel>> collect = sortReplacement.stream()
                .map(replacementAssembler::toCollectionModel)
                .collect(Collectors.toList());
        CollectionModel<CollectionModel<ReplacementModel>> models = CollectionModel.of(collect,
                linkTo(methodOn(ReplacementController.class).getAll()).withRel("replacements"),
                linkTo(methodOn(GroupController.class).get(id)).withSelfRel());
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @GetMapping("/teachers/")
    public ResponseEntity<?> getAll() {
        List<Teacher> teachers = teacherRepository.findAll();
        CollectionModel<TeacherModel> models = assembler.toCollectionModel(teachers);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping(value = "/teachers")
    public ResponseEntity<?> post(@RequestBody @Valid Teacher teacher) {
        TeacherModel model = assembler.toModel(teacherRepository.save(teacher));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping(value = "/teachers/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Teacher teacher) {
        TeacherModel model = teacherRepository.findById(id)
                .map(t -> {
                    t.setName(teacher.getName());
                    t.setLessons(teacher.getLessons());
                    t.setReplacements(teacher.getReplacements());
                    return teacherRepository.save(t);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        teacherRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/teachers")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(teacherRepository::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/teachers/")
    public ResponseEntity<?> deleteAll() {
        teacherRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
