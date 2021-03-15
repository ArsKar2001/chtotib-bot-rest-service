package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaReplacementRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
public class IReplacementService implements ReplacementService {
    private final JpaReplacementRepository replacementRepository;

    public IReplacementService(JpaReplacementRepository replacementRepository) {
        this.replacementRepository = replacementRepository;
    }

    @Override
    public Replacement save(Replacement replacement) {
        return replacementRepository.save(replacement);
    }

    @Override
    public void save(MultipartFile file) throws StringReadException, IOException {

    }
}
