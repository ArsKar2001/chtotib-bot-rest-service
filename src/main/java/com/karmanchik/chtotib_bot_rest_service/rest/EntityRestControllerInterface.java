package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface EntityRestControllerInterface<T extends BaseEntity> {
    ResponseEntity<?> get(@NotNull Integer id);

    CollectionModel<EntityModel<Group>> getAll();

    ResponseEntity<?> post(T t);

    ResponseEntity<?> put(@NotNull Integer id, T t);

    ResponseEntity<?> delete(@NotNull Integer id);

    ResponseEntity<?> deleteAll();
}
