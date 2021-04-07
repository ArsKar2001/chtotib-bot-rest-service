package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.TeacherModelAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/")
public class TeacherController extends BaseController<Teacher, TeacherService> {
    private final TeacherService teacherService;
    private final TeacherModelAssembler assembler;

    public TeacherController(TeacherService teacherService, TeacherModelAssembler assembler) {
        super(teacherService);
        this.teacherService = teacherService;
        this.assembler = assembler;
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

    @Override
    @GetMapping("/teachers/*")
    public ResponseEntity<?> getAll() {
        List<TeacherModel> collect = teacherService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return null;
    }

    @Override
    public ResponseEntity<?> post(Teacher teacher) {
        return null;
    }

    @Override
    public ResponseEntity<?> put(@NotNull Integer id, Teacher teacher) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAll(List<Integer> values) {
        return null;
    }
}
