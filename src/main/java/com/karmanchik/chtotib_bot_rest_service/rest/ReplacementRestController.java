package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class ReplacementRestController {
    private final ReplacementService replacementService;

    public ReplacementRestController(ReplacementService replacementService) {
        this.replacementService = replacementService;
    }

    @GetMapping("/replacements")
    public ResponseEntity<?> findAll() {
        List<Replacement> all = replacementService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/replacements/{id}")
    public ResponseEntity<?> findById(@Valid @PathVariable("id") Integer id) {
        try {
            Replacement replacement = replacementService.findById(id);
            return ResponseEntity.ok(replacement);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/insert/replacement")
    public ResponseEntity<?> createReplacement(@Valid @RequestBody Replacement replacement) {
        Replacement save = replacementService.save(replacement);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/replacements/{id}")
    public ResponseEntity<?> updateReplacement(@Valid @PathVariable("id") Integer id,
                                               @Valid @RequestBody Replacement replacementDetails) {
        try {
            Replacement replacement = replacementService.findById(id);
            replacement.setDate(replacementDetails.getDate());
            replacement.setLessons(replacementDetails.getLessons());
            replacement.setGroup(replacementDetails.getGroup());
            return ResponseEntity.ok(replacement);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/replacements/{id}")
    public ResponseEntity<?> deleteReplacement(@Valid @PathVariable("id") Integer id) {
        try {
            Replacement replacement = replacementService.findById(id);
            replacementService.delete(replacement);
            return ResponseEntity.ok(replacement);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
