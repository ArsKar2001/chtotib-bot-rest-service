package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.ReplacementAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ReplacementController implements Controller<Replacement> {
    private final JpaReplacementRepository replacementRepository;
    private final ReplacementAssembler assembler;


    @Override
    @GetMapping("/replacements/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        ReplacementModel model = replacementRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/replacements")
    public ResponseEntity<?> getAllByDay(@RequestParam @NotNull LocalDate date) {
        List<Replacement> lessons = replacementRepository.findAllByDateOrderByPairNumber(date);
        CollectionModel<ReplacementModel> models = assembler.toCollectionModel(lessons)
                .add(linkTo(methodOn(ReplacementController.class).getAllByDay(date)).withRel("day_" + date));
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @GetMapping("/replacements/")
    public ResponseEntity<?> getAll() {
        List<Replacement> replacements = replacementRepository.findAll();
        CollectionModel<ReplacementModel> models = assembler.toCollectionModel(replacements);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping("/replacements")
    public ResponseEntity<?> post(@RequestBody @Valid Replacement replacement) {
        ReplacementModel model = assembler.toModel(replacementRepository.save(replacement));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping("/replacements/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Replacement replacement) {
        ReplacementModel model = replacementRepository.findById(id)
                .map(r -> {
                    r.setDate(replacement.getDate());
                    r.setGroup(replacement.getGroup());
                    r.setTeachers(replacement.getTeachers());
                    r.setDiscipline(replacement.getDiscipline());
                    r.setPairNumber(replacement.getPairNumber());
                    r.setAuditorium(replacement.getAuditorium());
                    return replacementRepository.save(r);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/replacements/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        replacementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/replacements")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(replacementRepository::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/replacements/")
    public ResponseEntity<?> deleteAll() {
        replacementRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
