package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.service.FileUploadService;
import com.karmanchik.chtotib_bot_rest_service.service.schedule.ScheduleService;
import com.karmanchik.chtotib_bot_rest_service.service.word.WordService;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j
@RestController
@RequestMapping("/api/")
public class FileUploadRestController {
    private final FileUploadService fileUploadService;
    private final WordService wordService;
    private final ScheduleService scheduleService;

    public FileUploadRestController(FileUploadService fileUploadService, WordService wordService, ScheduleService scheduleService) {
        this.fileUploadService = fileUploadService;
        this.wordService = wordService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/upload")
    public @ResponseBody
    String getMesInfo() {
        return "Можете загружать файлы";
    }

    @PostMapping("/upload")
    public @ResponseBody
    String handleFileUpload(MultipartFile file) throws IOException {
        final Thread thread = new Thread(fileUploadService);
        thread.setName("importTimetable");
        getMesInfo("");
        if (!file.isEmpty()) {
            final String text = wordService.getText(file.getInputStream());
            final var json = scheduleService.createScheduleAsJSON(text);
            fileUploadService.setJsonArray(json);
            thread.start();
            log.info("Start thread: " + thread.getName() + "; timetable - " + json.toString());
            return "Начали процедуру импорта " + file.getName() + "...";
        } else
            return "Файл пустой!";
    }

    private void getMesInfo(String s) {

    }
}
