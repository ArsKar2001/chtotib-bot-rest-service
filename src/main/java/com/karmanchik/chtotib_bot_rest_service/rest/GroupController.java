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
import com.karmanchik.chtotib_bot_rest_service.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
public class GroupController extends BaseController<Group, GroupService> {
    private final GroupAssembler assembler;
    private final LessonAssembler lessonAssembler;
    private final ReplacementAssembler replacementAssembler;
    private final GroupService groupService;

    public GroupController(GroupService groupService,
                           GroupAssembler assembler,
                           LessonAssembler lessonAssembler,
                           ReplacementAssembler replacementAssembler) {
        super(groupService);
        this.groupService = groupService;
        this.assembler = assembler;
        this.lessonAssembler = lessonAssembler;
        this.replacementAssembler = replacementAssembler;
    }

    @Override
    @GetMapping("/groups/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        GroupModel model = groupService.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/groups/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<Lesson> lessons = groupService.getLessonsByGroupId(id);
        List<List<Lesson>> sortLessons = new ArrayList<>();
        lessons.stream()
                .map(Lesson::getDay)
                .sorted()
                .distinct()
                .forEach(day -> sortLessons.add(lessons.stream()
                        .filter(lesson -> lesson.getDay().equals(day))
                        .sorted(Comparator.comparing(Lesson::getPairNumber))
                        .collect(Collectors.toList())));
        List<CollectionModel<LessonModel>> collect = sortLessons.stream()
                .map(lessonAssembler::toCollectionModel)
                .map(model -> model.add(linkTo(methodOn(GroupController.class)
                        .getLessons(id)).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(collect);
    }

    @GetMapping("/groups/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<Replacement> replacements = groupService.getReplacementsByGroupId(id);
        List<List<Replacement>> sortReplacements = new ArrayList<>();
        replacements.stream()
                .map(Replacement::getDate)
                .sorted()
                .distinct()
                .forEach(date -> sortReplacements.add(replacements.stream()
                        .filter(replacement -> replacement.getDate().equals(date))
                        .sorted(Comparator.comparing(Replacement::getPairNumber))
                        .collect(Collectors.toList())));
        List<CollectionModel<ReplacementModel>> collect = sortReplacements.stream()
                .map(replacementAssembler::toCollectionModel)
                .map(model -> model.add(linkTo(methodOn(GroupController.class)
                        .getLessons(id)).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(collect);
    }

    @Override
    @GetMapping("/groups/")
    public ResponseEntity<?> getAll() {
        List<Group> groups = groupService.findAll();
        CollectionModel<GroupModel> models = assembler.toCollectionModel(groups);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping("/groups")
    public ResponseEntity<?> post(@RequestBody @Valid Group group) {
        GroupModel model = assembler.toModel(groupService.save(group));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping("/groups/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Group group) {
        GroupModel model = groupService.findById(id)
                .map(g -> {
                    g.setName(group.getName());
                    g.setLessons(group.getLessons());
                    g.setReplacements(group.getReplacements());
                    return groupService.save(g);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteAll(List<Integer> values) {
        values.forEach(groupService::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        groupService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
