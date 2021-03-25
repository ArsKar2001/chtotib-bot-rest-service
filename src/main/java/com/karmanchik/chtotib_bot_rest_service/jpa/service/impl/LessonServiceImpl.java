package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaLessonsRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final JpaLessonsRepository lessonsRepository;

    @Override
    public <S extends Lesson> S save(S s) {
        return (S) lessonsRepository
                .getByGroupIdAndDayAndPairNumber(
                        s.getGroupId(),
                        s.getDay(),
                        s.getPairNumber())
                .orElseGet(() -> lessonsRepository.save(s));
    }
}
