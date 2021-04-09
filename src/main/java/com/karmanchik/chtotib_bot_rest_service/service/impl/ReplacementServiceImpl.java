package com.karmanchik.chtotib_bot_rest_service.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ReplacementServiceImpl implements ReplacementService {
    private final JpaReplacementRepository replacementRepository;

    public ReplacementServiceImpl(JpaReplacementRepository replacementRepository) {
        this.replacementRepository = replacementRepository;
    }

    @Override
    public Replacement save(Replacement replacement) {
        log.info("Save the replacement {}", replacement.getId());
        return replacementRepository.save(replacement);
    }

    @Override
    public List<Replacement> saveAll(List<Replacement> t) {
        log.info("Save the replacements: {}", t);
        return replacementRepository.saveAll(t);
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Delete the replacement {}", id);
        replacementRepository.deleteById(id);
    }

    @Override
    @DeleteMapping("/replacements/")
    public void deleteAll(List<Replacement> t) {
        replacementRepository.deleteAll(t);
    }

    @Override
    public void deleteAll() {
        log.info("Delete the replacements...");
        replacementRepository.deleteAll();
        log.info("Delete the replacements... OK");
    }

    @Override
    public Optional<Replacement> findById(Integer id) {
        log.info("Find the replacement {}", id);
        return replacementRepository.findById(id);
    }

    @Override
    public List<Replacement> findAll() {
        log.info("Find the replacements...");
        return replacementRepository.findAll();
    }

    @Override
    public List<Replacement> findAllByDate(@NotNull LocalDate date) {
        return replacementRepository.findAllByDateOrderByPairNumber(date);
    }
}
