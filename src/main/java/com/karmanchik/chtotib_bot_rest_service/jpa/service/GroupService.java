package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;

import java.util.List;
import java.util.Optional;

public interface GroupService extends BaseService<Group> {
    Optional<Group> getByName(String name);
    List<Lesson> getLessonsByGroupId(Integer groupId);
    List<Replacement> getReplacementsByGroupId(Integer groupId);
}
