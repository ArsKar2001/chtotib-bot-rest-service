package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GroupServiceImpl implements GroupService {
    private final JpaGroupRepository groupRepository;

    public GroupServiceImpl(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group save(Group group) {
        log.info("Save the group {}", group.getId());
        return groupRepository.save(group);
    }

    public List<Group> saveAll(List<Group> t) {
        log.info("Save the groups {}", t.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return groupRepository.saveAll(t);
    }

    public void deleteById(Integer id) {
        log.info("Delete the group {}", id);
        groupRepository.deleteById(id);
    }

    public void deleteAll(List<Group> t) {
        log.info("Delete the groups {}", t.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        groupRepository.deleteAll(t);
    }

    public void deleteAll() {
        log.info("Delete the all groups");
        groupRepository.deleteAll();
    }

    public Optional<Group> findById(Integer id) {
        log.info("Find the group {}", id);
        return groupRepository.findById(id);
    }

    public List<Group> findAll() {
        log.info("Find the all group");
        return groupRepository.findAll();
    }

    @Override
    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        return groupRepository.getLessonsById(groupId);
    }

    @Override
    public List<Replacement> getReplacementsByGroupId(Integer groupId) {
        return groupRepository.getReplacementsById(groupId);
    }
}
