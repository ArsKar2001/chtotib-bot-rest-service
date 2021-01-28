package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.parser.WordParser;
import com.karmanchik.chtotib_bot_rest_service.task.ImportTask;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Log4j
@RestController
@RequestMapping("/api/files/")
public class FileUploadRestController {
    private ImportTask importTask;
    private final WordParser wordParser;

    public FileUploadRestController(ImportTask importTask, WordParser wordParser) {
        this.importTask = importTask;
        this.wordParser = wordParser;
    }

    @GetMapping("/upload")
    public @ResponseBody
    String getMesInfo() {
        return "Можете загружать файлы";
    }

    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(MultipartFile file) {
        final Thread thread = new Thread(importTask);
        if (!file.isEmpty()) {
            try (InputStream stream = file.getInputStream()) {
                final var timetable = wordParser.createTimetable(stream);
                importTask.setJsonArray(timetable);
                thread.start();
                log.info("Start thread: "+thread.toString());
                return "Начали процедуру импорта "+file.getName()+"...";
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return "Не удалось загрузить файл: "+e.getMessage();
            }
        } else
            return "Файл пустой!";
    }
}
