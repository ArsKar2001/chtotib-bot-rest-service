package com.karmanchik.chtotib_bot_rest_service.assembler.model;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.rest.GroupController;
import com.karmanchik.chtotib_bot_rest_service.rest.LessonController;
import com.karmanchik.chtotib_bot_rest_service.rest.ReplacementController;
import com.karmanchik.chtotib_bot_rest_service.rest.TeacherController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Класс - помощник. Реальзует статические методы для преобразования сущностей в RepresentationModel
 */
public class ModelHelper {
    private ModelHelper() {
    }

    /**
     * Статический метод-фабрика для преобразования коллекции сущностей-Teachers в коллекцию RepresentationModel
     * @param teachers Коллекция @see Teachers
     * @return
     */
    public static List<TeacherModel> toTeachersModel(List<Teacher> teachers) {
        if (teachers.isEmpty()) {
            return Collections.emptyList();
        }
        return teachers.stream()
                .map(teacher -> TeacherModel.builder()
                        .id(teacher.getId())
                        .name(teacher.getName())
                        .build()
                        .add(linkTo(methodOn(TeacherController.class)
                                .get(teacher.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    public static GroupModel toGroupModel(Group group) {
        return GroupModel.builder()
                .id(group.getId())
                .name(group.getName())
                .build()
                .add(linkTo(methodOn(GroupController.class)
                        .get(group.getId()))
                        .withSelfRel());
    }
}
