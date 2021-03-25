package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import com.karmanchik.chtotib_bot_rest_service.parser.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@RestController
@RequestMapping("/api/v1/import/")
@RequiredArgsConstructor
public class FileImportRestController {
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    @PostMapping("/lessons")
    @ResponseBody
    public ResponseEntity<?> importLessons(MultipartFile[] files) {
        TimetableParser parser = new TimetableParser();
        Set<Lesson> lessons = new HashSet<>();

        for (MultipartFile file : files) {
            try (InputStream stream = file.getInputStream()) {
                log.info("Start import from file \"{}\"", file.getOriginalFilename());
                String text = Word.getText(stream);

                final List<List<String>> csv = parser.textToCSV(text);
                List<Teacher> teachers = getTeachers(csv);

//                parser.textToCSV(text).forEach(list -> {
//                    list.forEach(s -> {
//                        log.debug("Importing csv str: \"{}\"", s);
//                        String[] strings = s.split(";");
//                        String groupName = strings[0];
//                        Integer dayOfWeek = Integer.valueOf(strings[1]);
//                        Integer pairNumber = Integer.valueOf(strings[2]);
//                        String discipline = strings[3];
//                        String auditorium = strings[4];
//                        String teacherName = strings[5];
//                        WeekType weekType = WeekType.valueOf(strings[6]);
//
//                        Teacher teacher = teacherService.getByName(teacherName);
//                        log.debug("Get teacher {}", teacher.getId());
//                        Group group = groupService.getByName(groupName);
//                        log.debug("Get group {}", group.getId());
//
//                        Lesson lesson = lessonService.save(Lesson.builder()
//                                .group(group)
//                                .teacher(teacher)
//                                .day(dayOfWeek)
//                                .pairNumber(pairNumber)
//                                .discipline(discipline)
//                                .auditorium(auditorium)
//                                .weekType(weekType)
//                                .build());
//
//                        log.debug("Insert lesson {}", lesson);
//                        lessons.add(lesson);
//                    });
//                });
            } catch (IOException | InvalidFormatException | StringReadException e) {
                log.error("Ошибка файла: {}; {}", file.getOriginalFilename(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
            }
        }
        return ResponseEntity.ok(lessons);
    }

    private List<Teacher> getTeachers(List<List<String>> csv) {
        List<String> teachersNames = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        csv.forEach(list -> {
            list.forEach(s -> {
                final String[] strings = s.split(";");
                String teacherName = strings[5];
                teachersNames.add(teacherName);
            });
        });

        HashSet<String> uniqueTeachers = new HashSet<>(teachersNames);


        return null;
    }
}
