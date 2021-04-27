package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.model.GroupModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.rest.GroupController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    public GroupAssembler() {
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
        groupModel.setName(entity.getName());
        groupModel.setId(entity.getId());
        return groupModel;
    }

    @Override
    public CollectionModel<GroupModel> toCollectionModel(Iterable<? extends Group> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(GroupController.class).getAll()).withSelfRel());
    }
}
