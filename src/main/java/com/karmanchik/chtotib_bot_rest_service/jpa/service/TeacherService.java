package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends BaseService<Teacher> {

    <S extends Teacher> S getByName(String teacherName);

    List<Lesson> getLessonsByGroupId(Integer id);

    List<Replacement> getReplacementsByGroupId(Integer id);

    Optional<List<String>> getAllNames();
}
