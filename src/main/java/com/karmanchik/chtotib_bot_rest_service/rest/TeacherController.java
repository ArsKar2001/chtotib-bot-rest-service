package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.LessonAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.ReplacementAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.TeacherAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Log4j2
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TeacherController implements Controller<Teacher> {
    private final JpaTeacherRepository teacherRepository;
    private final TeacherAssembler teacherAssembler;
    private final LessonAssembler lessonAssembler;
    private final ReplacementAssembler replacementAssembler;

    @Override
    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        TeacherModel model = teacherRepository.findById(id)
                .map(teacherAssembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/teachers/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Lesson>> sortLessons = new ArrayList<>();
        List<Lesson> lessons = teacherRepository.getLessonsById(id);
        log.info("Получили пары для педагога {id={}}: {}", id, lessons);
        lessons.stream()
                .map(Lesson::getDay)
                .sorted()
                .distinct()
                .forEach(day -> sortLessons.add(lessons.stream()
                        .filter(lesson -> lesson.getDay().equals(day))
                        .sorted(Comparator.comparing(Lesson::getPairNumber))
                        .collect(Collectors.toList())));

        var collect = sortLessons.stream()
                .map(ll -> ll.stream()
                        .map(lessonAssembler::toModel)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        collect.forEach(lm -> mapList.add(
                Map.of(
                        "teacher_id", id,
                        "lessons", lm
                )));

        log.info("Построили модель: {}", mapList);
        return ResponseEntity.ok()
                .body(mapList);
    }

    @GetMapping("/teachers/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Replacement>> sortReplacements = new ArrayList<>();
        List<Replacement> replacements = teacherRepository.getReplacementsById(id);
        log.info("Получили замену для педагога {id={}}: {}", id, replacements);
        replacements.stream()
                .map(Replacement::getDate)
                .sorted()
                .distinct()
                .forEach(date -> sortReplacements.add(replacements.stream()
                        .filter(replacement -> replacement.getDate().equals(date))
                        .sorted(Comparator.comparing(Replacement::getPairNumber))
                        .collect(Collectors.toList())));

        var collect = sortReplacements.stream()
                .map(ll -> ll.stream()
                        .map(replacementAssembler::toModel)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        collect.forEach(lm -> mapList.add(
                Map.of(
                        "teacher_id", id,
                        "lessons", lm
                )));

        log.info("Построили модель: {}", mapList);
        return ResponseEntity.ok()
                .body(mapList);
    }

    @Override
    @GetMapping("/teachers/")
    public ResponseEntity<?> getAll() {
        var models = teacherAssembler.toCollectionModel(teacherRepository.findAll());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @PostMapping(value = "/teachers")
    public ResponseEntity<?> post(@RequestBody @Valid Teacher teacher) {
        if (teacherRepository.existsByName(teacher.getName())) {
            return ResponseEntity.badRequest().body("Педагог с таким именем уже существует.");
        }

        TeacherModel model = teacherAssembler.toModel(teacherRepository.save(teacher));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @PutMapping(value = "/teachers/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Teacher teacher) {
        if (teacherRepository.existsByName(teacher.getName())) {
            return ResponseEntity.badRequest().body("Педагог с таким именем уже существует.");
        }

        TeacherModel model = teacherRepository.findById(id)
                .map(t -> {
                    t.setName(teacher.getName());
                    t.setLessons(teacher.getLessons());
                    t.setReplacements(teacher.getReplacements());
                    return teacherRepository.save(t);
                }).map(teacherAssembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        teacherRepository.deleteById(id);
        return ResponseEntity.ok("OK");
    }

    @Override
    @DeleteMapping("/teachers")
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> values) {
        values.forEach(teacherRepository::deleteById);
        return ResponseEntity.ok("OK");
    }

    @Override
    @DeleteMapping("/teachers/")
    public ResponseEntity<?> deleteAll() {
        teacherRepository.deleteAll();
        return ResponseEntity.ok("OK");
    }
}
