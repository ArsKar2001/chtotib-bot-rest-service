package com.karmanchik.chtotib_bot_rest_service.rest.assembler;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.rest.Controller;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ModelAssembler<T extends BaseEntity> implements RepresentationModelAssembler<T, EntityModel<T>> {
    private final Class<? extends Controller<T>> controller;

    public ModelAssembler(Class<? extends Controller<T>> controller) {
        this.controller = controller;
    }

    @Override
    public EntityModel<T> toModel(T entity) {
        String rel = entity.getClass().getSimpleName().toLowerCase() + "s";
        return EntityModel.of(entity,
                linkTo(methodOn(controller).get(entity.getId())).withSelfRel(),
                linkTo(methodOn(controller).getAll()).withRel(rel));
    }
}
