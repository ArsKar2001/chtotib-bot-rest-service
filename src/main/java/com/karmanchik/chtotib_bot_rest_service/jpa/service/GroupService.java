package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;

import java.util.List;
import java.util.Optional;

public interface GroupService extends BaseService<Group> {
    <S extends Group> S getByName(String name);

    Optional<List<String>> getAllGroupName();

    List<Lesson> getLessonsByGroupId(Integer id);

    List<Replacement> getReplacementByGroupId(Integer id);
}
