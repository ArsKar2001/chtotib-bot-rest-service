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
@RequestMapping("/api/")
public class FileUploadRestController {
    private ImportTask importTask;

    public FileUploadRestController(ImportTask importTask) {
        this.importTask = importTask;
    }

    @GetMapping("/upload")
    public @ResponseBody
    String getMesInfo() {
        return "Можете загружать файлы";
    }

    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(MultipartFile file) throws IOException {
        final Thread thread = new Thread(importTask);
        thread.setName("importTimetable");
        if (!file.isEmpty()) {
            WordParser parser = new WordParser(file.getInputStream());
            final var timetable = parser.createTimetable();
            importTask.setJsonArray(timetable);
            thread.start();
            log.info("Start thread: "+thread.getName()+"; timetable - "+timetable.toString());
            return "Начали процедуру импорта "+file.getName()+"...";
        } else
            return "Файл пустой!";
    }
}
