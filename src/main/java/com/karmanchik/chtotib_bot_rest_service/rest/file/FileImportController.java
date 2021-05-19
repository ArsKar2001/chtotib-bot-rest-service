package com.karmanchik.chtotib_bot_rest_service.rest.file;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.entity.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.helper.LatinNumber;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaLessonsRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.karmanchik.chtotib_bot_rest_service.parser.sequence.Sequence.*;

@Log4j2
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class FileImportController {
    public static final Set<String> EXCEPTION_LIST = new HashSet<>();

    private final JpaLessonsRepository lessonsRepository;
    private final JpaGroupRepository groupRepository;
    private final JpaTeacherRepository teacherRepository;
    private final JpaReplacementRepository replacementRepository;

    @PostMapping("/import/replacements")
    public ResponseEntity<?> importReplacements(@RequestBody MultipartFile mFile) {
        EXCEPTION_LIST.clear();
        List<Replacement> replacements = new ArrayList<>();

        log.info("Find all groups...");
        List<Group> groups = Optional.of(groupRepository.findAll())
                .orElseThrow(() -> new ResourceNotFoundException("Не возможно импортировать замену, т.к. не найдено ни одной группы."));
        log.info("Find all groups: {}. OK", groups.size());
        log.info("Find all teachers...");
        List<Teacher> teachers = Optional.of(teacherRepository.findAll())
                .orElseThrow(() -> new ResourceNotFoundException("Не возможно импортировать замену, т.к. не найдено ни одного педагога."));
        log.info("Find all teachers: {}. OK", teachers.size());

        try {
            ReplacementParser parser = new ReplacementParser();
            String filename = mFile.getOriginalFilename();
            LocalDate date = parser.getDateFromFileName(filename);

            if (date == null)
                return ResponseEntity.badRequest()
                        .body("Ошибка файла: " + filename + "; " +
                                "иназвание файла не соответствует формату: \"З А М Е Н А  на день_недели номер_дня навание_месяца неделя нижняя/верхняя.\";\n" +
                                "Пример: \"З А М Е Н А  на среду 7 апреля неделя нижняя.\"");

            parser.parseToListMap(mFile.getBytes()).stream()
                    .flatMap(Collection::stream)
                    .forEach(map -> {
                        String groupName = (String) map.get("group_name");
                        String pair = (String) map.get("pair");
                        String discipline = (String) map.get("discipline");
                        String auditorium = (String) map.get("auditorium");
                        List<String> teacherNameList = ((List<String>) map.get("teachers"));
                        String validGroupName = ValidGroupName.getValidGroupName(groupName);
                        Group group = groups.stream()
                                .filter(g -> g.getName().equalsIgnoreCase(validGroupName))
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(groupName, Group.class));
                        List<Teacher> teachersByRepl = new ArrayList<>();
                        for (String s : teacherNameList) {
                            if (!s.trim().isBlank()) {
                                if (ValidTeacherName.isTeacher(s)) {
                                    teachersByRepl.add(teachers.stream()
                                            .filter(teacher -> teacher.getName().equalsIgnoreCase(s))
                                            .findFirst()
                                            .orElseThrow(() -> new ResourceNotFoundException(s, Teacher.class)));
                                } else {
                                    EXCEPTION_LIST.add(new StringReadException(s, "Иванов А.А.").getMessage());
                                }
                            }
                        }
                        replacements.add(Replacement.builder()
                                .date(date)
                                .discipline(discipline)
                                .auditorium(auditorium)
                                .group(group)
                                .teachers(teachersByRepl)
                                .pairNumber(pair)
                                .build());
                    });
            if (!EXCEPTION_LIST.isEmpty()) {
                replacementRepository.deleteAll();
                log.info("Save replacements [{}]...", replacements.size());
                return ResponseEntity.ok()
                        .body(replacementRepository.saveAll(replacements));
            } else {
                return ResponseEntity.badRequest()
                        .body(EXCEPTION_LIST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/import/lessons")
    public ResponseEntity<?> importLessons(@RequestBody MultipartFile[] mFiles) {
        EXCEPTION_LIST.clear();
        try {
            if (mFiles.length > 2) return ResponseEntity.badRequest().body("Файлов должно быть не больше 2.");

            Set<String> uniqueTeacherNames = new HashSet<>();
            Set<String> uniqueGroupNames = new HashSet<>();
            List<String> csv = getCSVListOfFile(mFiles);
            for (String s : csv) {
                String[] ss = s.split(CSV_SPLIT);
                String groupName = ss[0];
                String teacherNames = ss[5];

                List<String> teachersByStr = teachersStrToList(teacherNames);

                uniqueGroupNames.add(groupName);
                uniqueTeacherNames.addAll(teachersByStr);
            }

            List<Lesson> lessons = new ArrayList<>();
            List<Group> groups = uniqueGroupNames.stream()
                    .map(s -> groupRepository.findByName(s)
                            .orElseGet(() -> groupRepository.save(Group.builder(s).build())))
                    .collect(Collectors.toList());
            List<Teacher> allTeachers = uniqueTeacherNames.stream()
                    .map(s -> teacherRepository.findByName(s)
                            .orElseGet(() -> teacherRepository.save(Teacher.builder(s).build())))
                    .collect(Collectors.toList());

            for (String s : csv) {
                String[] ss = s.split(CSV_SPLIT);
                if (ss.length > CSV_COLUMN_SIZE)
                    EXCEPTION_LIST.add(new StringReadException(s, ss.length).getMessage());
                String groupName = ss[0];
                String dayStr = ss[1];
                String pair = ss[2];
                String discipline = ss[3];
                String auditorium = ss[4];
                String teachersName = ss[5];
                String weekTypeStr = ss[6];
                int day = Integer.parseInt(dayStr);
                Integer pairNumber = LatinNumber.get(pair);
                WeekType weekType = WeekType.valueOf(weekTypeStr);

                List<String> teachersStr = teachersStrToList(teachersName);
                List<Teacher> teachersByPair = new ArrayList<>();
                teachersStr.forEach(s1 -> allTeachers.stream()
                        .filter(teacher -> teacher.getName().equalsIgnoreCase(s1))
                        .forEach(teachersByPair::add));
                Group group = groups.stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
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

            if (EXCEPTION_LIST.isEmpty()) {
                deleteLessons();

                log.info("Importing lessons...");
                lessonsRepository.saveAll(lessons);
                log.info("Importing lessons... OK");
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", "fail",
                                "trace", EXCEPTION_LIST
                        ));
            }
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

    private void deleteLessons() {
        log.info("Delete the lessons...");
        lessonsRepository.deleteAll();
        log.info("Delete the lessons... OK");
    }

    private List<String> teachersStrToList(String teacherNames) throws StringReadException {
        List<String> teacherList = new ArrayList<>();
        for (String s : teacherNames.split(DEFAULT_SPLIT)) {
            String s1 = s.trim();
            Matcher matcher = ValidTeacherName.getPattern().matcher(s1);
            if (matcher.matches()) {
                teacherList.add(ValidTeacherName.getValidTeacherName(s1, matcher));
            } else
                EXCEPTION_LIST.add(new StringReadException(s, "Иванов А.А.").getMessage());
        }
        return teacherList;
    }

    private List<String> getCSVListOfFile(MultipartFile[] files) throws IOException, InvalidFormatException {
        TimetableParser parser = new TimetableParser();
        List<String> csvList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            parser.parse(multipartFile.getBytes())
                    .forEach(csvList::addAll);
        }
        return csvList;
    }
}
