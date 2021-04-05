package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
    public <S extends Replacement> S save(S s) {
        return replacementRepository.save(s);
    }

    @Override
    public List<Replacement> saveAll(List<Replacement> t) {
        return replacementRepository.saveAll(t);
    }

    @Override
    public void deleteById(Integer id) {
        replacementRepository.deleteById(id);
    }

    @Override
    public <S extends Replacement> void delete(S s) {
        replacementRepository.delete(s);
    }

    @Override
    public void deleteAll() {
        replacementRepository.deleteAll();
    }

    @Override
    public Optional<Replacement> findById(Integer id) {
        return replacementRepository.findById(id);
    }

    @Override
    public List<Replacement> findAll() {
        return replacementRepository.findAll();
    }
}
