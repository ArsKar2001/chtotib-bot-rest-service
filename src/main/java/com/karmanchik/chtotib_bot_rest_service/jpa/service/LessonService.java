package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;

import java.util.List;

public interface LessonService extends BaseService<Lesson> {
    @Override
    <S extends Lesson> S save(S s);

    @Override
    <S extends Lesson> List<S> saveAll(List<S> s);
}
