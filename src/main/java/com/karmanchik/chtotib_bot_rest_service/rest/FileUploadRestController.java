package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.parser.WordParser;
import com.karmanchik.chtotib_bot_rest_service.task.ImportTask;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j
@RestController
@RequestMapping("/api/files/")
public class FileUploadRestController {

    private final ImportTask importTask;

    public FileUploadRestController(ImportTask importTask) {
        this.importTask = importTask;
    }

    @GetMapping("/upload")
    public @ResponseBody
    String uploadInfo() {
        return "Можете загружать файл.";
    }

    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file) {
        WordParser wordParser = new WordParser();
        if (!file.isEmpty()) {
            try {
                var json = wordParser.createTimetable(file.getInputStream());
                importTask.setJsonArray(json);
                new Thread(importTask).start();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                return "Error: " + e.getMessage();
            }
            return "Начали процедуру импорта...";
        } else {
            return "Не удалось загрузить " + name + " потому что файл пустой.";
        }
    }
}
