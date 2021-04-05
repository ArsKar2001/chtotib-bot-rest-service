package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final JpaTeacherRepository teacherRepository;
    private final EntityManager entityManager;

    @Override
    public <S extends Teacher> S getByName(String teacherName) {
        return (S) teacherRepository.getByName(teacherName)
                .orElseGet(() -> teacherRepository
                        .save(Teacher.builder(teacherName)
                                .build()));
    }

    @Override
    public <S extends Teacher> S save(S s) {
        return teacherRepository.save(s);
    }

    @Override
    public List<Teacher> saveAll(List<Teacher> t) {
        return teacherRepository.saveAll(t);
    }

    @Override
    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public <S extends Teacher> void delete(S s) {
        teacherRepository.delete(s);
    }

    @Override
    public void deleteAll() {
        log.info("Deleted all users!");
        teacherRepository.deleteAllInBatch();
    }

    @Override
    public List<Lesson> getLessonsByGroupId(Integer id) {
        return teacherRepository.getLessonsById(id);
    }

    @Override
    public List<Replacement> getReplacementsByGroupId(Integer id) {
        return teacherRepository.getReplacementsById(id);
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        return teacherRepository.findById(id);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<List<String>> getAllNames() {
        return teacherRepository.getAllNames();
    }
}
