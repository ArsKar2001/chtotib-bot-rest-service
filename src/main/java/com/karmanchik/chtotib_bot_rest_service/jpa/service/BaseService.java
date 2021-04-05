package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseEntity> {
    <S extends T> S save(S s);

    List<T> saveAll(List<T> t);

    void deleteById(Integer id);

    <S extends T> void delete(S s);

    void deleteAll();

    Optional<T> findById(Integer id);

    List<T> findAll();
}
