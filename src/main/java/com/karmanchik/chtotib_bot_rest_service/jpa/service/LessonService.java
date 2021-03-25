package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;

public interface LessonService extends BaseService<Lesson> {
    @Override
    <S extends Lesson> S save(S s);
}
