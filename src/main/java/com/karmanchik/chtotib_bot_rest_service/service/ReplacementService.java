package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReplacementService {
    Replacement save(Replacement replacement);
    void save(MultipartFile file) throws StringReadException, IOException;
}
