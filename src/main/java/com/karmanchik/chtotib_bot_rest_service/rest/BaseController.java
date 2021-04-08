package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.BaseService;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;

public abstract class BaseController<T extends BaseEntity, S extends BaseService<T>> implements Controller<T, S> {
    private final S s;

    public BaseController(S s) {
        this.s = s;
    }
}
