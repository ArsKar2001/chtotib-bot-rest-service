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

public class ModelHelper {
    private ModelHelper() {
    }

    public static List<ReplacementModel> toReplacementsModel(List<Replacement> replacements) {
        if (replacements.isEmpty()) {
            return Collections.emptyList();
        }
        return replacements.stream()
                .map(replacement -> ReplacementModel.builder()
                        .id(replacement.getId())
                        .date(replacement.getDate())
                        .discipline(replacement.getDiscipline())
                        .auditorium(replacement.getAuditorium())
                        .pairNumber(replacement.getPairNumber())
                        .build()
                        .add(linkTo(methodOn(ReplacementController.class)
                                .get(replacement.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    public static List<LessonModel> toLessonsModel(List<Lesson> lessons) {
        if (lessons.isEmpty()) {
            return Collections.emptyList();
        }
        return lessons.stream()
                .map(lesson -> LessonModel.builder()
                        .id(lesson.getId())
                        .day(lesson.getDay())
                        .discipline(lesson.getDiscipline())
                        .auditorium(lesson.getAuditorium())
                        .pairNumber(lesson.getPairNumber())
                        .weekType(lesson.getWeekType())
                        .build()
                        .add(linkTo(methodOn(LessonController.class)
                                .get(lesson.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

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
