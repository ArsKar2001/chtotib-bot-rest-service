package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.dto.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.rest.TeacherController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeacherModelAssembler extends RepresentationModelAssemblerSupport<Teacher, TeacherModel> {
    public TeacherModelAssembler() {
        super(TeacherController.class, TeacherModel.class);
    }

    @Override
    public TeacherModel toModel(Teacher entity) {
        TeacherModel teacherModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(TeacherController.class).get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(TeacherController.class).getAll())
                        .withRel("teachers"))
                .add(linkTo(methodOn(TeacherController.class).getLessons(entity.getId()))
                        .withRel("lessons"))
                .add(linkTo(methodOn(TeacherController.class).getReplacements(entity.getId()))
                        .withRel("replacements"));;
        teacherModel.setId(entity.getId());
        teacherModel.setName(entity.getName());
        return null;
    }

    @Override
    public CollectionModel<TeacherModel> toCollectionModel(Iterable<? extends Teacher> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(TeacherController.class).getAll()).withSelfRel());
    }
}
