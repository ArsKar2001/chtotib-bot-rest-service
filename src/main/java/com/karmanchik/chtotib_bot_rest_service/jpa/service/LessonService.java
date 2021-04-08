package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    List<Lesson> findAllByGroups(List<Group> groups);
    List<Lesson> findAllByTeachers(List<Teacher> teachers);

    List<Lesson> findAllByDay(Integer day);
}
