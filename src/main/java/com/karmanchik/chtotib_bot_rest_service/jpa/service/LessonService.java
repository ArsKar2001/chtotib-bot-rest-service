package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    List<Lesson> findAllByGroup(Group group);
    List<Lesson> findAllByTeacher(Teacher teacher);
}
