package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaLessonsRepository;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class LessonServiceImpl implements LessonService {
    private final JpaLessonsRepository lessonsRepository;

    public LessonServiceImpl(JpaLessonsRepository lessonsRepository) {
        this.lessonsRepository = lessonsRepository;
    }

    @Override
    public Lesson save(Lesson lesson) {
        log.info("Save the lesson {}...", lesson.getId());
        return lessonsRepository.save(lesson);
    }

    @Override
    public List<Lesson> saveAll(List<Lesson> t) {
        log.info("Save the lessons: {}", t);
        return lessonsRepository.saveAll(t);
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Delete the lesson {}", id);
        lessonsRepository.deleteById(id);
    }

    @Override
    @DeleteMapping("api/lessons/")
    public void deleteAll(List<Lesson> t) {
        lessonsRepository.deleteAll(t);
    }

    @Override
    public void deleteAll() {
        log.info("Delete the lessons...");
        lessonsRepository.deleteAll();
        log.info("Delete the lessons... OK");
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        log.info("Find the lesson {}", id);
        return lessonsRepository.findById(id);
    }

    @Override
    public List<Lesson> findAll() {
        log.info("Find the lessons");
        return lessonsRepository.findAll();
    }

    @Override
    public List<Lesson> findAllByGroups(List<Group> groups) {
        return lessonsRepository.findAllByGroupsOrderByDayPairNumberAsc(groups);
    }

    @Override
    public List<Lesson> findAllByTeachers(List<Teacher> teachers) {
        return lessonsRepository.findAllByTeachersOOrderByDayPairNumberAsc(teachers);
    }

    @Override
    public List<Lesson> findAllByDay(Integer day) {
        return lessonsRepository.findAllByDayOOrderByPairNumber(day);
    }
}
