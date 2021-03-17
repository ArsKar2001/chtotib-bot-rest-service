package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

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
    public Object save(MultipartFile file) throws StringReadException {
        try (InputStream stream = file.getInputStream()) {
            final ReplacementParser parser = new ReplacementParser(stream);
            final JSONArray json = new JSONArray(parser.parse());
            System.out.println(json.toString());
            return json.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
