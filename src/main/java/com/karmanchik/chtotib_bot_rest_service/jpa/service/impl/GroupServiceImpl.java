package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final JpaGroupRepository groupRepository;

    @Override
    public <S extends Group> S getByName(String name) {
        return (S) groupRepository.getByName(name)
                .orElseGet(() -> groupRepository
                        .save(Group.builder(name)
                                .build()));
    }

    @Override
    public Optional<List<String>> getAllGroupName() {
        return groupRepository.getAllGroupName();
    }

    @Override
    public <S extends Group> S save(S s) {
        return groupRepository.save(s);
    }

    @Override
    public List<Group> saveAll(List<Group> t) {
        return groupRepository.saveAll(t);
    }

    @Override
    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

    @Override
    public <S extends Group> void delete(S s) {
        groupRepository.delete(s);
    }

    @Override
    public void deleteAll() {
        log.info("Deleted all groups!");
        groupRepository.deleteAllInBatch();
    }

    @Override
    public List<Lesson> getLessonsByGroupId(Integer id) {
        return groupRepository.getLessonsById(id);
    }

    @Override
    public List<Replacement> getReplacementByGroupId(Integer id) {
        return groupRepository.getReplacementsById(id);
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
