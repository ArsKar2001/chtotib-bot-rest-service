package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
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
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
        assembler = new ModelAssembler<>(this.getClass());
    }

    @GetMapping("/teachers/{id}")
    public EntityModel<Teacher> get(@PathVariable @NotNull Integer id) {
        Teacher teacher = teacherService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return assembler.toModel(teacher);
    }

    @GetMapping("/teachers")
    public CollectionModel<EntityModel<Teacher>> getAll() {
        List<EntityModel<Teacher>> teachers = teacherService.findAll().stream()
                .map(assembler::toModel)
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
    public ResponseEntity<?> deleteAll() {
        teacherService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
