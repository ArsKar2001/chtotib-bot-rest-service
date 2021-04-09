package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TeacherServiceImpl implements TeacherService {
    private final JpaTeacherRepository teacherRepository;

    public TeacherServiceImpl(JpaTeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher save(Teacher teacher) {
        log.info("Save the teacher {}", teacher.getId());
        return teacherRepository.save(teacher);
    }

    public List<Teacher> saveAll(List<Teacher> t) {
        log.info("Save the teachers {}", t.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return teacherRepository.saveAll(t);
    }

    public void deleteById(Integer id) {
        log.info("Delete the teacher {}", id);
        teacherRepository.deleteById(id);
    }

    public void deleteAll(List<Teacher> t) {
        log.info("Delete the teachers {}", t.stream()
                .map(Teacher::getName)
                .collect(Collectors.toList()));
        teacherRepository.deleteAll(t);
    }

    public void deleteAll() {
        log.info("Delete the all teachers...");
        teacherRepository.deleteAll();
        log.info("Delete the all teachers... OK");
    }

    public Optional<Teacher> findById(Integer id) {
        log.info("Find the teacher {}", id);
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        log.info("Find the teachers");
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getByName(String name) {
        return teacherRepository.getByName(name);
    }

    @Override
    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        return teacherRepository.getLessonsById(teacherId);
    }

    @Override
    public List<Replacement> getReplacementsByTeacherId(Integer teacherId) {
        return teacherRepository.getReplacementsById(teacherId);
    }
}
