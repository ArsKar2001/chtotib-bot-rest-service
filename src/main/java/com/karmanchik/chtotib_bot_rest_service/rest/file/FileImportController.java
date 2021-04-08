package com.karmanchik.chtotib_bot_rest_service.rest.file;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.model.NumberLesson;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class FileImportController {
    private static final int MAX_COLUMN = 7;

    @PostMapping("/import/lessons")
    public ResponseEntity<?> importLessons(@RequestBody MultipartFile[] files) {
        if (files.length > 2) return ResponseEntity.badRequest().body("Файлов должно быть не больше 2.");

        for (MultipartFile file : files) {
            try {
                for (var table : new TimetableParser((File) file).parse()) {
                    for (var row : table) {
                        if (row.size() < MAX_COLUMN) {
                            String groupNamesStr = row.get(0);
                            String dayStr = row.get(1);
                            String pairStr = row.get(2);
                            String discipline = row.get(3);
                            String auditorium = row.get(4);
                            String teacherNamesStr = row.get(5);
                            String weekTypeStr = row.get(6);
                        } else throw new StringReadException(row, row.size());
                    }
                }
            } catch (IOException | InvalidFormatException | RuntimeException e) {
                log.error(e.getMessage(), e);
                return ResponseEntity.badRequest()
                        .body(e.getMessage());
            }
        }
    }
}
