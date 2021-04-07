package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;

import java.util.List;

public interface TeacherService extends BaseService<Teacher> {
    List<Lesson> getLessonsByTeacherId(Integer teacherId);
    List<Replacement> getReplacementsByTeacherId(Integer teacherId);
}
