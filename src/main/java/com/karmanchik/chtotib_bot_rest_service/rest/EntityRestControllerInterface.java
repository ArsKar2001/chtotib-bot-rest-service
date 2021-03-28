package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface EntityRestControllerInterface<T extends BaseEntity> {
    ResponseEntity<?> get(@NotNull Integer id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> post(T t);

    <S extends T> ResponseEntity<?> put(@NotNull Integer id, @Valid @NotNull S s);

    ResponseEntity<?> delete(@NotNull Integer id);

    <S extends T> ResponseEntity<?> delete(S s);

    ResponseEntity<?> deleteAll();
}
