package com.karmanchik.chtotib_bot_rest_service.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReplacementService {
    private final WordService wordService;

    public ReplacementService(WordService wordService) {
        this.wordService = wordService;
    }

}
