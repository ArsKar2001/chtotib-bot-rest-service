package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;

import java.util.List;

public interface ReplacementService extends BaseService<Replacement> {
    List<Replacement> findAllByGroup(Group group);
    List<Replacement> findAllByTeacher(Teacher teacher);
}
