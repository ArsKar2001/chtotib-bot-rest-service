package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;

public interface BaseService<T extends BaseEntity> {
    <S extends T> S save(S s);
}
