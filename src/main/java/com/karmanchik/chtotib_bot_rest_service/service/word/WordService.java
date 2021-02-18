package com.karmanchik.chtotib_bot_rest_service.service.word;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface WordService {
    String getText(InputStream stream);
}
