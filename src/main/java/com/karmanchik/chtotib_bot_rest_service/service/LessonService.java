package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    List<Lesson> findAllByDay(Integer day);
}
