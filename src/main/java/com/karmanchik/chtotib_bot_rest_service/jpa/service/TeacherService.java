package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;

import java.util.List;

public interface TeacherService extends BaseService<Teacher> {

    @Override
    <S extends Teacher> S save(S s);

    @Override
    <S extends Teacher> List<S> saveAll(List<S> s);

    <E> E getByName(String teacherName);
}
