package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;

public interface GroupService extends BaseService<Group> {
    @Override
    <S extends Group> S save(S s);

    <E> E getByName(String groupName);
}
