package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    T save(T t);

    List<T> saveAll(List<T> t);

    void deleteById(Integer id);

    void deleteAll(List<T> t);

    void deleteAll();

    Optional<T> findById(Integer id);

    List<T> findAll();
}
