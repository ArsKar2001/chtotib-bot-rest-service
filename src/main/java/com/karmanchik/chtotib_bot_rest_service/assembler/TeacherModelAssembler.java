package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.model.ModelHelper;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.TeacherModel;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.rest.TeacherController;
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
                        .withRel("teachers"));
        teacherModel.setId(entity.getId());
        teacherModel.setName(entity.getName());
        teacherModel.setLessons(ModelHelper.toLessonsModel(entity.getLessons()));
        teacherModel.setReplacements(ModelHelper.toReplacementsModel(entity.getReplacements()));
        return null;
    }
}
