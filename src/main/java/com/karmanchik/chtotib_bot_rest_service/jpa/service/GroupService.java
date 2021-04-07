package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;

import java.util.List;

public interface GroupService extends BaseService<Group> {
    List<Lesson> getLessonsByGroupId(Integer groupId);
    List<Replacement> getReplacementsByGroupId(Integer groupId);
}
