package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
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
public class TeacherController implements Controller<Teacher> {
    private final ModelAssembler<Teacher> assembler;
    private final ModelAssembler<Lesson> assemblerLesson = new ModelAssembler<>(LessonController.class);
    private final ModelAssembler<Replacement> assemblerReplacement = new ModelAssembler<>(ReplacementController.class);
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
        assembler = new ModelAssembler<>(this.getClass());
    }

    @GetMapping("/teachers/{id}")
    public EntityModel<Teacher> get(@PathVariable @NotNull Integer id) {
        Teacher teacher = teacherService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return assembler.toModel(teacher)
                .add(linkTo(methodOn(TeacherController.class).getLessons(id)).withRel("lessons"))
                .add(linkTo(methodOn(TeacherController.class).getReplacements(id)).withRel("replacements"));
    }

    @GetMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Lesson> lessons = teacherService.getLessonsByTeacherId(id);
        List<EntityModel<Lesson>> models = lessons.stream()
                .map(assemblerLesson::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Lesson>> model = CollectionModel.of(models,
                linkTo(methodOn(GroupController.class).getLessons(id)).withSelfRel());
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @GetMapping("/teachers/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Replacement> replacements = teacherService.getReplacementsByTeacherId(id);
        List<EntityModel<Replacement>> models = replacements.stream()
                .map(assemblerReplacement::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Replacement>> model = CollectionModel.of(models,
                linkTo(methodOn(GroupController.class).getLessons(id)).withSelfRel());
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @GetMapping("/teachers")
    public CollectionModel<EntityModel<Teacher>> getAll() {
        List<EntityModel<Teacher>> teachers = teacherService.findAll().stream()
                .map(teacher -> assembler.toModel(teacher)
                        .add(linkTo(methodOn(TeacherController.class).getLessons(teacher.getId())).withRel("lessons"))
                        .add(linkTo(methodOn(TeacherController.class).getReplacements(teacher.getId())).withRel("replacements")))
                .collect(Collectors.toList());
        return CollectionModel.of(teachers,
                linkTo(methodOn(TeacherController.class).getAll()).withSelfRel());
    }

    @PostMapping("/teachers")
    public ResponseEntity<?> post(@RequestBody @Valid Teacher teacher) {
        EntityModel<Teacher> model = assembler.toModel(teacherService.save(teacher));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Teacher teacher) {
        Teacher teach = teacherService.findById(id)
                .map(t -> {
                    t.setName(teacher.getName());
                    return teacherService.save(t);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        EntityModel<Teacher> model = assembler.toModel(teach);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/teachers")
    public ResponseEntity<?> deleteAll(List<Integer> values) {
        values.forEach(teacherService::deleteById);
        return ResponseEntity.noContent().build();
    }
}
