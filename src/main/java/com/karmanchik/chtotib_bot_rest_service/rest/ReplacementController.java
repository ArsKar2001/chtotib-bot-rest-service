package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.ReplacementService;
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
public class ReplacementController implements Controller<Replacement> {
    private final ModelAssembler<Replacement> assembler;
    private final ReplacementService replacementService;

    public ReplacementController(ReplacementService replacementService) {
        this.replacementService = replacementService;
        assembler = new ModelAssembler<>(this.getClass());
    }

    @Override
    @GetMapping("/replacements/{id}")
    public EntityModel<Replacement> get(@PathVariable @NotNull Integer id) {
        Replacement replacement = replacementService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        return assembler.toModel(replacement);
    }

    @Override
    @GetMapping("/replacements/")
    public CollectionModel<EntityModel<Replacement>> getAll() {
        List<EntityModel<Replacement>> replacements = replacementService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(replacements,
                linkTo(methodOn(ReplacementController.class).getAll()).withSelfRel());
    }

    @Override
    @PostMapping("/replacements")
    public ResponseEntity<?> post(@RequestBody @Valid Replacement replacement) {
        EntityModel<Replacement> model = assembler.toModel(replacementService.save(replacement));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping("/replacements/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Replacement replacement) {
        Replacement rep = replacementService.findById(id)
                .map(r -> {
                    r.setAuditorium(replacement.getAuditorium());
                    r.setDate(replacement.getDate());
                    r.setDiscipline(replacement.getDiscipline());
                    r.setPairNumber(replacement.getPairNumber());
                    r.setGroup(replacement.getGroup());
                    r.setTeacher(replacement.getTeacher());
                    return replacementService.save(r);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        EntityModel<Replacement> model = assembler.toModel(rep);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/replacements/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        replacementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/replacements")
    public ResponseEntity<?> deleteAll(@RequestParam("values") List<Integer> values) {
        values.forEach(replacementService::deleteById);
        return ResponseEntity.noContent().build();
    }
}
