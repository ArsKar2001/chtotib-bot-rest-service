package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.*;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
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
import java.time.LocalDate;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/import/")
@RequiredArgsConstructor
public class FileImportRestController {
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ReplacementService replacementService;

    @PostMapping("/lessons")
    @ResponseBody
    public ResponseEntity<?> importTimetable(MultipartFile[] files) {
        TimetableParser parser = new TimetableParser();

        List<Lesson> lessons = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();

        List<String> teacherNames = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();

        groupService.deleteAll();
        teacherService.deleteAll();
        lessonService.deleteAll();
        replacementService.deleteAll();


        for (MultipartFile file : files) {
            try (InputStream stream = file.getInputStream()) {
                String text = Word.getText(stream);
                JSONArray array = parser.textToJSON(text);
                array.toList().stream()
                        .filter(JSONObject.class::isInstance)
                        .map(JSONObject.class::cast)
                        .forEach(json -> {
                            String groupName = json.getString("group_name");
                            String teacherName = json.getString("teacher_name");
                            teacherNames.add(teacherName);
                            groupNames.add(groupName);
                        });
            } catch (IOException | InvalidFormatException | StringReadException e) {
                log.error("Ошибка ипорта файла: {}; {}; {}", file.getOriginalFilename(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        teacherNames.stream()
                .distinct()
                .map(s -> Teacher.builder(s).build())
                .forEach(teachers::add);
        groupNames.stream()
                .distinct()
                .map(s -> Group.builder(s).build())
                .forEach(groups::add);

        for (MultipartFile file : files) {
            try (InputStream stream = file.getInputStream()) {
                String text = Word.getText(stream);
                JSONArray array = parser.textToJSON(text);

                for (Object o : array) {
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
                            .orElseThrow(() -> new ResourceNotFoundException(teacherName, Teacher.class));
                    Group group = groups.stream()
                            .filter(g -> g.getName().equalsIgnoreCase(groupName))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(groupName, Group.class));

                    lessons.add(Lesson.builder()
                            .group(group)
                            .groupName(groupName)
                            .teacher(teacher)
                            .teacherName(teacherName)
                            .day(day)
                            .pairNumber(pair)
                            .discipline(discipline)
                            .auditorium(auditorium)
                            .weekType(weekType)
                            .build());
                }
            } catch (IOException | InvalidFormatException | StringReadException | ResourceNotFoundException e) {
                log.error("Ошибка ипорта файла: {}; {}; {}", file.getOriginalFilename(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        log.info("Importing groups...");
        groupService.saveAll(groups);
        log.info("Importing groups... OK");
        log.info("Importing teacher...");
        teacherService.saveAll(teachers);
        log.info("Importing teacher... OK");
        log.info("Importing lessons...");
        lessonService.saveAll(lessons);
        log.info("Importing lessons... OK");

        HashMap<String, Integer> response = new HashMap<>();
        response.put("lessons", lessons.size());
        response.put("groups", groups.size());
        response.put("teacher", teachers.size());
        response.put("all", teachers.size() + lessons.size() + teachers.size());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/replacement")
    public ResponseEntity<?> importReplacement(MultipartFile file) {
        log.info("Start import data from file \"{}\"", file.getOriginalFilename());
        try (InputStream stream = file.getInputStream()) {
            ReplacementParser parser = new ReplacementParser();

            log.info("Delete all replacement.");
            replacementService.deleteAll();

            log.info("Find all groups from db...");
            List<Group> groups = groupService.findAll();
            log.info("Find all groups from db... OK");
            log.info("Find all teachers from db...");
            List<Teacher> teachers = teacherService.findAll();
            log.info("Find all teachers from db... OK");


            List<Replacement> replacements = new ArrayList<>();

            String text = Word.getText(stream);
            JSONArray array = parser.textToJSON(text);
            log.debug("Parsed text to JSON: {}", array);
            for (Object o : array) {
                JSONObject json = (JSONObject) o;
                String groupName = json.getString("group_name");
                String pairNumber = json.getString("pair_number");
                String discipline = json.getString("discipline");
                String auditorium = json.getString("auditorium");
                String teacherName = json.getString("teacher_name");
                LocalDate date = (LocalDate) json.get("date");

                Group group = groups.stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(groupName, Group.class));
                Teacher teacher = teachers.stream()
                        .filter(t -> t.getName().equalsIgnoreCase(teacherName))
                        .findFirst()
                        .orElse(null);

                replacements.add(
                        Replacement.builder()
                                .group(group)
                                .groupName(groupName)
                                .teacher(teacher)
                                .teacherName(teacherName)
                                .date(date)
                                .discipline(discipline)
                                .auditorium(auditorium)
                                .pairNumber(pairNumber)
                                .build()
                );
            }
            log.info("Importing replacements...");
            replacementService.saveAll(replacements);
            log.info("Importing replacements... OK");
            return ResponseEntity.ok(Map.of("replacement", replacements.size()));
        } catch (IOException | InvalidFormatException | StringReadException | ResourceNotFoundException e) {
            log.error("Ошибка ипорта файла: {}; {}; {}", file.getOriginalFilename(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
