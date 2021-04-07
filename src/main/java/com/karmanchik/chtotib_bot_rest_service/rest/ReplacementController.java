package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class ReplacementController extends BaseController<Replacement, ReplacementService> {
    private final ReplacementService replacementService;

    public ReplacementController(ReplacementService replacementService) {
        super(replacementService);
        this.replacementService = replacementService;
    }

    @Override
    public ResponseEntity<?> get(@NotNull Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> post(Replacement replacement) {
        return null;
    }

    @Override
    public ResponseEntity<?> put(@NotNull Integer id, Replacement replacement) {
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
