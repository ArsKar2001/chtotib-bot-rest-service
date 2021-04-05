package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class ReplacementController implements EntityRestControllerInterface<Replacement> {
    private final ReplacementService replacementService;

    public ReplacementController(ReplacementService replacementService) {
        this.replacementService = replacementService;
    }

    @Override
    @GetMapping("/replacements/{id}")
    public Replacement get(@PathVariable @NotNull Integer id) {
        return replacementService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
    }

    @Override
    @GetMapping("/replacements/")
    public List<Replacement> getAll() {
        return replacementService.findAll();
    }

    @Override
    @PostMapping("/replacements/")
    public Replacement post(@RequestBody @Valid Replacement replacement) {
        return replacementService.save(replacement);
    }

    @Override
    @PutMapping("/replacements/{id}")
    public Replacement put(@PathVariable @NotNull Integer id,
                           @RequestBody @Valid Replacement replacement) {
        return replacementService.findById(id)
                .map(r -> {
                    r.setAuditorium(replacement.getAuditorium());
                    r.setDate(replacement.getDate());
                    r.setDiscipline(replacement.getDiscipline());
                    r.setPairNumber(replacement.getPairNumber());
                    r.setGroup(replacement.getGroup());
                    r.setTeacher(replacement.getTeacher());
                    return replacementService.save(r);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
    }

    @Override
    @DeleteMapping("/replacements/{id}")
    public void delete(@PathVariable @NotNull Integer id) {
        replacementService.deleteById(id);
    }

    @Override
    @DeleteMapping("/replacements/")
    public void deleteAll() {
        replacementService.deleteAll();
    }
}
