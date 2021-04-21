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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class GroupController implements Controller<Group> {
    private final GroupAssembler assembler;
    private final LessonAssembler lessonAssembler;
    private final ReplacementAssembler replacementAssembler;
    private final JpaGroupRepository groupRepository;


    @Override
    @GetMapping("/groups/{id}")
    public ResponseEntity<?> get(@PathVariable @NotNull Integer id) {
        GroupModel model = groupRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/groups/{id}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable @NotNull Integer id) {
        List<List<Lesson>> sortLessons = new ArrayList<>();
        List<Lesson> lessons = groupRepository.getLessonsById(id);
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
        CollectionModel<CollectionModel<LessonModel>> models = CollectionModel.of(collect,
                linkTo(methodOn(LessonController.class).getAll()).withRel("lessons"),
                linkTo(methodOn(GroupController.class).get(id)).withSelfRel());
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @GetMapping("/groups/{id}/replacements")
    public ResponseEntity<?> getReplacements(@PathVariable @NotNull Integer id) {
        List<List<Replacement>> sortReplacements = new ArrayList<>();
        List<Replacement> replacements = groupRepository.getReplacementsById(id);
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
        CollectionModel<CollectionModel<ReplacementModel>> models = CollectionModel.of(collect,
                linkTo(methodOn(ReplacementController.class).getAll()).withRel("replacements"),
                linkTo(methodOn(GroupController.class).get(id)).withSelfRel());
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @GetMapping("/groups/")
    public ResponseEntity<?> getAll() {
        List<Group> groups = groupRepository.findAll();
        CollectionModel<GroupModel> models = assembler.toCollectionModel(groups);
        return ResponseEntity.created(models.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(models);
    }

    @Override
    @PostMapping(value = "/groups")
    public ResponseEntity<?> post(@RequestBody @Valid Group group) {
        GroupModel model = assembler.toModel(groupRepository.save(group));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @PutMapping(value = "/groups/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Group group) {
        GroupModel model = groupRepository.findById(id)
                .map(g -> {
                    g.setName(group.getName());
                    g.setLessons(group.getLessons());
                    g.setReplacements(group.getReplacements());
                    return groupRepository.save(g);
                }).map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @Override
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        groupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteAll(List<Integer> values) {
        values.forEach(groupRepository::deleteById);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        groupRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
