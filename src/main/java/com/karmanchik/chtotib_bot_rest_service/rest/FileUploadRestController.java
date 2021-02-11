package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.service.WordService;
import com.karmanchik.chtotib_bot_rest_service.service.ImportService;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j
@RestController
@RequestMapping("/api/")
public class FileUploadRestController {
    private final ImportService importService;

    public FileUploadRestController(ImportService importService) {
        this.importService = importService;
    }

    @GetMapping("/upload")
    public @ResponseBody
    String getMesInfo() {
        return "Можете загружать файлы";
    }

    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(MultipartFile file) throws IOException {
        final Thread thread = new Thread(importService);
        thread.setName("importTimetable");
        if (!file.isEmpty()) {
            WordService parser = new WordService(file.getInputStream());
            final var timetable = parser.createTimetable();
            importService.setJsonArray(timetable);
            thread.start();
            log.info("Start thread: " + thread.getName() + "; timetable - " + timetable.toString());
            return "Начали процедуру импорта " + file.getName() + "...";
        } else
            return "Файл пустой!";
    }
}
