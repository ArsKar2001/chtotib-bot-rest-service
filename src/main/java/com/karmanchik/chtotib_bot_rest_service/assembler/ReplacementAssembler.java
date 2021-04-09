package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.model.ModelHelper;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ReplacementModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.rest.ReplacementController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReplacementAssembler extends RepresentationModelAssemblerSupport<Replacement, ReplacementModel> {
    public ReplacementAssembler() {
        super(ReplacementController.class, ReplacementModel.class);
    }

    @Override
    public ReplacementModel toModel(Replacement entity) {
        ReplacementModel replacementModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(ReplacementController.class).get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(ReplacementController.class).getAll())
                        .withRel("replacements"));
        replacementModel.setId(entity.getId());
        replacementModel.setGroup(ModelHelper.toGroupModel(entity.getGroup()));
        replacementModel.setTeachers(ModelHelper.toTeachersModel(entity.getTeachers()));
        replacementModel.setDate(entity.getDate());
        replacementModel.setDiscipline(entity.getDiscipline());
        replacementModel.setAuditorium(entity.getAuditorium());
        replacementModel.setPairNumber(entity.getPairNumber());
        return replacementModel;
    }

    @Override
    public CollectionModel<ReplacementModel> toCollectionModel(Iterable<? extends Replacement> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(ReplacementController.class).getAll()).withSelfRel());
    }
}
