package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final JpaGroupRepository groupRepository;
    private final EntityManager entityManager;

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
    public <S extends Group> List<S> saveAll(List<S> s) {
        return groupRepository.saveAll(s);
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
        return (List<Lesson>) entityManager.createQuery(
                "SELECT l FROM Lesson l " +
                        "WHERE l.group.id = :id " +
                        "ORDER BY l.day, l.pairNumber")
                .setParameter("id", id).getResultList();
    }

    @Override
    public List<Replacement> getReplacementByGroupId(Integer id) {
        return (List<Replacement>) entityManager.createQuery(
                "SELECT r FROM Replacement r " +
                        "WHERE r.group.id = :id " +
                        "ORDER BY r.date, r.pairNumber")
                .setParameter("id", id).getResultList();
    }

    @Override
    public <S extends Group> Optional<S> findById(Integer id) {
        return (Optional<S>) groupRepository.findById(id);
    }

    @Override
    public <S extends Group> List<S> findAll() {
        return (List<S>) groupRepository.findAll();
    }
}
