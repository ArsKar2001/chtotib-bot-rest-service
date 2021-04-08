package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface ReplacementService extends BaseService<Replacement> {
    List<Replacement> findAllByGroups(List<Group> groups);
    List<Replacement> findAllByTeachers(List<Teacher> teachers);

    List<Replacement> findAllByDate(@NotNull LocalDate date);
}
