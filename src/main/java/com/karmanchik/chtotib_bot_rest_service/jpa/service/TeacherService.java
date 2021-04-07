package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;

import java.util.List;

public interface TeacherService extends BaseService<Teacher> {
    List<Lesson> getLessonsByTeacherId(Integer teacherId);
    List<Replacement> getReplacementsByTeacherId(Integer teacherId);
}
