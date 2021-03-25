package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final JpaGroupRepository groupRepository;

    @Override
    public Group getByName(String groupName) {
        return groupRepository.getByName(groupName)
                .orElseGet(() -> groupRepository
                        .save(Group.builder(groupName)
                                .build()));
    }

    @Override
    public <S extends Group> S save(S s) {
        return null;
    }
}
