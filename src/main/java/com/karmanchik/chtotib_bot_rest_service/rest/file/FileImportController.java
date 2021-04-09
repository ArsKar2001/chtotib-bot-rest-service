package com.karmanchik.chtotib_bot_rest_service.rest.file;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidTeacherName;
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
import java.util.*;
import java.util.regex.Matcher;

import static com.karmanchik.chtotib_bot_rest_service.parser.Sequence.CSV_SPLIT;
import static com.karmanchik.chtotib_bot_rest_service.parser.Sequence.DEFAULT_SPLIT;

@Log4j2
@RestController
@RequestMapping("/api/")
public class FileImportController {

    @PostMapping("/import/lessons")
    public ResponseEntity<?> importLessons(@RequestBody MultipartFile[] files) {
        try {
            if (files.length > 2) return ResponseEntity.badRequest().body("Файлов должно быть не больше 2.");
            Set<String> uniqueTeacherNames = new HashSet<>();
            Set<String> uniqueGroupNames = new HashSet<>();
            List<String> lessonsCsvString = new ArrayList<>();
            for (String s : getCSVListByFiles(files)) {
                String[] ss = s.split(CSV_SPLIT);
                String groupName = ss[0];
                String teacherNames = ss[5];

                List<String> teachersByStr = teachersStrToList(teacherNames, ss);

                uniqueGroupNames.add(groupName);
                uniqueTeacherNames.addAll(teachersByStr);
            }

        } catch (RuntimeException | IOException | InvalidFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    private List<String> teachersStrToList(String teacherNames, String[] ss) throws StringReadException {
        List<String> teacherList = new ArrayList<>();
        for (String s : teacherNames.split(DEFAULT_SPLIT)) {
            String s1 = s.trim();
            Matcher matcher = ValidTeacherName.getPattern().matcher(s1);
            if (matcher.matches()) {
                teacherList.add(ValidTeacherName.getValidTeacherName(s1, matcher));
            } else
                throw new StringReadException(ss, s, "Иванов А.А.");
        }
        return teacherList;
    }

    private List<String> getCSVListByFiles(MultipartFile[] files) throws IOException, InvalidFormatException {
        TimetableParser parser = new TimetableParser();
        List<String> csvList = new ArrayList<>();
        for (MultipartFile file : files)
            parser.parse(MultipartFileToFileConverter.convert(file))
                    .forEach(csvList::addAll);
        return csvList;
    }
}
