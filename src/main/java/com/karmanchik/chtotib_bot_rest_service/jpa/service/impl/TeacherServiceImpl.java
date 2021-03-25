package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final JpaTeacherRepository teacherRepository;

    @Override
    public Teacher getByName(String teacherName) {
        return teacherRepository.getByName(teacherName)
                .orElseGet(() -> teacherRepository
                        .save(Teacher.builder()
                                .name(teacherName)
                                .build()));
    }

    @Override
    public <S extends Teacher> S save(S s) {
        return null;
    }
}
