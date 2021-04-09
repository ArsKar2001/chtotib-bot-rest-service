package com.karmanchik.chtotib_bot_rest_service.rest.file;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import com.karmanchik.chtotib_bot_rest_service.model.NumberLesson;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidTeacherName;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.karmanchik.chtotib_bot_rest_service.parser.Sequence.*;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class FileImportController {
    private final LessonService lessonService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final UserService userService;

    @PostMapping("/import/lessons")
    public ResponseEntity<?> importLessons(@RequestBody MultipartFile[] files) {
        try {
            if (files.length > 2) return ResponseEntity.badRequest().body("Файлов должно быть не больше 2.");

            Set<String> uniqueTeacherNames = new HashSet<>();
            Set<String> uniqueGroupNames = new HashSet<>();
            List<String> lessonsCsvString = new ArrayList<>();
            List<String> csv = getCSVListByFiles(files);
            for (String s : csv) {
                String[] ss = s.split(CSV_SPLIT);
                String groupName = ss[0];
                String teacherNames = ss[5];

                List<String> teachersByStr = teachersStrToList(teacherNames, ss);

                uniqueGroupNames.add(groupName);
                uniqueTeacherNames.addAll(teachersByStr);
            }

            List<Lesson> lessons = new ArrayList<>();
            List<Group> groups = uniqueGroupNames.stream()
                    .map(s -> groupService.getByName(s)
                            .orElse(Group.builder(s).build()))
                    .collect(Collectors.toList());
            List<Teacher> allTeachers = uniqueTeacherNames.stream()
                    .map(s -> teacherService.getByName(s)
                            .orElse(Teacher.builder(s).build()))
                    .collect(Collectors.toList());

            for (String s : csv) {
                String[] ss = s.split(CSV_SPLIT);

                if (ss.length > CSV_COLUMN_SIZE)
                    throw new StringReadException(s, ss.length);

                String groupName = ss[0];
                String dayStr = ss[1];
                String pair = ss[2];
                String discipline = ss[3];
                String auditorium = ss[4];
                String teachersName = ss[5];
                String weekTypeStr = ss[6];

                int day = Integer.parseInt(dayStr);
                Integer pairNumber = NumberLesson.get(pair);
                WeekType weekType = WeekType.valueOf(weekTypeStr);

                List<Teacher> teachersByPair = new ArrayList<>();
                allTeachers.forEach(teacher -> {
                    if (teachersName.toLowerCase().contains(teacher.getName().toLowerCase()))
                        teachersByPair.add(teacher);
                });
                Group group = groups.stream()
                        .filter(g -> g.getName().contains(groupName))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(groupName, Group.class));

                lessons.add(Lesson.builder()
                        .day(day)
                        .weekType(weekType)
                        .group(group)
                        .teachers(teachersByPair)
                        .discipline(discipline)
                        .auditorium(auditorium)
                        .pairNumber(pairNumber)
                        .build());
            }

            log.info("Importing groups...");
            groupService.saveAll(groups);
            log.info("Importing groups... OK");

            log.info("Importing lessons...");
            lessonService.saveAll(lessons);
            log.info("Importing lessons... OK");

            log.info("Importing teachers...");
            teacherService.saveAll(allTeachers);
            log.info("Importing teachers... OK");


            return ResponseEntity.ok(Map.of(
                    "groups", groups.size(),
                    "teachers", allTeachers.size(),
                    "lessons", lessons.size()
            ));
        } catch (RuntimeException | IOException | InvalidFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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