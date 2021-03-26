package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaTeacherRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final JpaTeacherRepository teacherRepository;

    @Override
    public Teacher getByName(String teacherName) {
        return teacherRepository.getByName(teacherName)
                .orElseGet(() -> teacherRepository
                        .save(Teacher.builder(teacherName)
                                .build()));
    }

    @Override
    public <S extends Teacher> S save(S s) {
        return teacherRepository.save(s);
    }

    @Override
    public <S extends Teacher> List<S> saveAll(List<S> s) {
        return teacherRepository.saveAll(s);
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
    public <S extends Teacher> void deleteAll() {
        log.info("Deleted all users!");
        teacherRepository.deleteAllInBatch();
    }

    @Override
    public <S extends Teacher> Optional<S> findById(Integer id) {
        return (Optional<S>) teacherRepository.findById(id);
    }

    @Override
    public <S extends Teacher> List<S> findAll() {
        log.info("");
        return (List<S>) teacherRepository.findAll();
    }
}
