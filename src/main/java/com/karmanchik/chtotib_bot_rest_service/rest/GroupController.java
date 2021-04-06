package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.rest.assembler.ModelAssembler;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/api/")
public class GroupController implements Controller<Group> {
    private final ModelAssembler<Group> assembler;
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
        assembler = new ModelAssembler<>(this.getClass());
    }

    @GetMapping("/groups/{id}")
    public EntityModel<Group> get(@PathVariable @NotNull Integer id) {
        Group group = groupService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        return assembler.toModel(group);
    }

    @GetMapping("/groups")
    public CollectionModel<EntityModel<Group>> getAll() {

        List<EntityModel<Group>> groups = groupService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(groups,
                linkTo(methodOn(GroupController.class).getAll()).withSelfRel());
    }

    @PostMapping("/groups")
    public ResponseEntity<?> post(@RequestBody @Valid Group group) {
        EntityModel<Group> model = assembler.toModel(groupService.save(group));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/groups/{id}")
    public ResponseEntity<?> put(@PathVariable @NotNull Integer id,
                                 @RequestBody @Valid Group group) {
        Group g1 = groupService.findById(id)
                .map(g -> {
                    g.setName(group.getName());
                    return groupService.save(g);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
        EntityModel<Group> model = assembler.toModel(g1);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/groups")
    public ResponseEntity<?> deleteAll() {
        groupService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
