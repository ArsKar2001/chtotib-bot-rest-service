package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/import/")
@RequiredArgsConstructor
public class FileImportRestController {
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final UserService userService;

    @PostMapping("/lessons")
    @ResponseBody
    public ResponseEntity<?> importTimetable(MultipartFile[] files) {
        TimetableParser parser = new TimetableParser();

        List<Lesson> lessons = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        Set<String> teacherNames = new HashSet<>();
        Set<String> groupNames = new HashSet<>();

        userService.deleteAll();
        groupService.deleteAll();
        teacherService.deleteAll();
        lessonService.deleteAll();

        for (MultipartFile file : files) {
            try (InputStream stream = file.getInputStream()) {
                log.info("Start import from file \"{}\"", file.getOriginalFilename());
                String text = Word.getText(stream);
                JSONArray array = parser.textToJSON(text);

                array.forEach(o -> {
                    JSONObject item = (JSONObject) o;
                    final String groupName = item.getString("group_name");
                    final String teacherName = item.getString("teacher_name");

                    teacherNames.add(teacherName);
                    groupNames.add(groupName);
                });

                teacherNames.forEach(s ->
                        teachers.add(Teacher.builder().name(s).build()));

                groupNames.forEach(s ->
                        groups.add(Group.builder(s).build()));

                array.forEach(o -> {
                    JSONObject item = (JSONObject) o;
                    final String groupName = item.getString("group_name");
                    final int day = item.getInt("day");
                    final int pair = item.getInt("pair");
                    final String discipline = item.getString("discipline");
                    final String auditorium = item.getString("auditorium");
                    final String teacherName = item.getString("teacher_name");
                    final WeekType weekType = item.getEnum(WeekType.class, "week");

                    Teacher teacher = teachers.stream()
                            .filter(t -> t.getName().equalsIgnoreCase(teacherName))
                            .findFirst()
                            .orElseThrow(IllegalAccessError::new);
                    Group group = groups.stream()
                            .filter(g -> g.getName().equalsIgnoreCase(groupName))
                            .findFirst()
                            .orElseThrow(IllegalAccessError::new);

                    lessons.add(Lesson.builder()
                            .group(group)
                            .teacher(teacher)
                            .day(day)
                            .pairNumber(pair)
                            .discipline(discipline)
                            .auditorium(auditorium)
                            .weekType(weekType)
                            .build());
                });

                log.info("Importing groups...");
                groupService.saveAll(groups);
                log.info("Importing teacher...");
                teacherService.saveAll(teachers);
                log.info("Importing lessons...");
                lessonService.saveAll(lessons);

            } catch (IOException | InvalidFormatException | StringReadException e) {
                log.error("Ошибка ипорта файла: {}; {}; {}", file.getOriginalFilename(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        HashMap<String, Integer> response = new HashMap<>();
        response.put("lessons", lessons.size());
        response.put("groups", groups.size());
        response.put("teacher", teachers.size());
        response.put("all", teachers.size() + lessons.size() + teachers.size());
        return ResponseEntity.ok().body(response);
    }
}
