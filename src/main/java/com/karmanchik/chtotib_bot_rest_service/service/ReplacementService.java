package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReplacementService {
    Replacement save(Replacement replacement);

    Object save(MultipartFile file) throws StringReadException, IOException;

    Replacement findById(Integer id) throws ResourceNotFoundException;

    List<Replacement> findAll();

    void deleteById(Integer id);

    void delete(Replacement replacement);
}
