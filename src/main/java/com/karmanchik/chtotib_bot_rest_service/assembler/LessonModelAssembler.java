package com.karmanchik.chtotib_bot_rest_service.assembler;

import com.karmanchik.chtotib_bot_rest_service.assembler.model.LessonModel;
import com.karmanchik.chtotib_bot_rest_service.assembler.model.ModelHelper;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.rest.LessonController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LessonModelAssembler extends RepresentationModelAssemblerSupport<Lesson, LessonModel> {
    public LessonModelAssembler() {
        super(LessonController.class, LessonModel.class);
    }

    @Override
    public LessonModel toModel(Lesson entity) {
        LessonModel lessonModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(LessonController.class).get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(LessonController.class).getAll())
                        .withRel("lessons"))
                .add(linkTo(methodOn(LessonController.class).getAllByDay(entity.getDay()))
                        .withRel("lessons_by_day"));
        lessonModel.setId(entity.getId());
        lessonModel.setDay(entity.getDay());
        lessonModel.setGroups(ModelHelper.toGroupsModel(entity.getGroups()));
        lessonModel.setTeachers(ModelHelper.toTeachersModel(entity.getTeachers()));
        lessonModel.setDiscipline(entity.getDiscipline());
        lessonModel.setAuditorium(entity.getAuditorium());
        lessonModel.setPairNumber(entity.getPairNumber());
        lessonModel.setWeekType(entity.getWeekType());
        return lessonModel;
    }

    @Override
    public CollectionModel<LessonModel> toCollectionModel(Iterable<? extends Lesson> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(LessonController.class).getAll()).withSelfRel());
    }
}
