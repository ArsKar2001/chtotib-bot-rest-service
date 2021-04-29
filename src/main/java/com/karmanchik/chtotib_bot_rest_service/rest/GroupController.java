package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.GroupAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.LessonAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.ReplacementAssembler;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.GroupModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping(value = "/api/")
@RequiredArgsConstructor
public class GroupController implements Controller<Group> {
    private final GroupAssembler assembler;
    private final LessonAssembler lessonAssembler;
    private final ReplacementAssembler replacementAssembler;
    private final JpaGroupRepository groupRepository;


    @Override
    @GetMapping("/groups/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        log.info("Поиск группы по id {}...", id);
        GroupModel model = groupRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        log.info("Построили модель: {}", model);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/groups/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Lesson>> sortLessons = new ArrayList<>();
        List<Lesson> lessons = groupRepository.getLessonsById(id);
        log.info("Получили пары для группы {id={}}: {}", id, lessons);
        lessons.stream()
                .map(Lesson::getDay)
                .sorted()
                .distinct()
                .forEach(day -> sortLessons.add(lessons.stream()
                        .filter(lesson -> lesson.getDay().equals(day))
                        .sorted(Comparator.comparing(Lesson::getPairNumber))
                        .collect(Collectors.toList())));

        List<List<LessonModel>> collect = sortLessons.stream()
                .map(ll -> ll.stream()
                        .map(lessonAssembler::toModel)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        collect.forEach(lm -> mapList.add(
                Map.of(
                        "group_id", id,
                        "lessons", lm
                )));

        log.info("Построили модель: {}", mapList);
        return ResponseEntity.ok()
                .body(mapList);
    }

    @GetMapping("/groups/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<List<Replacement>> sortLessons = new ArrayList<>();
        List<Replacement> replacements = groupRepository.getReplacementsById(id);
        log.info("Получили замену для группы {id={}}: {}", id, replacements);
        replacements.stream()
                .map(Replacement::getDate)
                .sorted()
                .distinct()
                .forEach(date -> sortLessons.add(replacements.stream()
                        .filter(replacement -> replacement.getDate().equals(date))
                        .sorted(Comparator.comparing(Replacement::getPairNumber))
                        .collect(Collectors.toList())));

        List<List<ReplacementModel>> collect = sortLessons.stream()
                .map(ll -> ll.stream()
                        .map(replacementAssembler::toModel)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        collect.forEach(lm -> mapList.add(
                Map.of(
                        "group_id", id,
                        "replacements", lm
                )));

        log.info("Построили модель: {}", mapList);
        return ResponseEntity.ok()
                .body(mapList);
    }

    @Override
    @GetMapping("/groups/")
    public ResponseEntity<?> getAll() {
        List<Group> groups = groupRepository.findAll();
        List<GroupModel> models = groups.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(models);
    }

    @Override
    @PostMapping(value = "/groups")
    public ResponseEntity<?> post(@RequestBody @Valid Group group) {
        if (groupRepository.existsByName(group.getName())) {
            return ResponseEntity.badRequest().body("Группа с таким названием уже существует.");
        }
        GroupModel model = assembler.toModel(groupRepository.save(group));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @PutMapping(value = "/groups/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Group group) {
        if (groupRepository.existsByName(group.getName())) {
            return ResponseEntity.badRequest().body("Группа с таким названием уже существует.");
        }
        GroupModel model = groupRepository.findById(id)
                .map(g -> {
                    g.setName(group.getName());
                    return assembler.toModel(groupRepository.save(g));
                }).orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(Map.of(
                        "status", "OK",
                        "objects", model
                ));
    }

    @Override
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        groupRepository.deleteById(id);
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<?> deleteAll(@RequestParam List<Integer> ids) {
        ids.forEach(groupRepository::deleteById);
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        groupRepository.deleteAll();
        return ResponseEntity.ok("OK");
    }
}
