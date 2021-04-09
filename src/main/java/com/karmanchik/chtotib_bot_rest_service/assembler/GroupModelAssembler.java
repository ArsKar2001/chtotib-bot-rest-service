package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.dto.GroupModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.rest.GroupController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupModelAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {
    public GroupModelAssembler() {
        super(GroupController.class, GroupModel.class);
    }

    @Override
    public GroupModel toModel(Group entity) {
        GroupModel groupModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(GroupController.class)
                        .get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(GroupController.class).getAll())
                        .withRel("groups"))
                .add(linkTo(methodOn(GroupController.class).getLessons(entity.getId()))
                        .withRel("lessons"))
                .add(linkTo(methodOn(GroupController.class).getReplacements(entity.getId()))
                        .withRel("replacements"));
        groupModel.setId(entity.getId());
        groupModel.setName(entity.getName());
        return groupModel;
    }

    @Override
    public CollectionModel<GroupModel> toCollectionModel(Iterable<? extends Group> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(GroupController.class).getAll()).withSelfRel());
    }
}
