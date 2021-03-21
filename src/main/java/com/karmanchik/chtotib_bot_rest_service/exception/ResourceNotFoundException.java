package com.karmanchik.chtotib_bot_rest_service.exception;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(Integer resourceId, Class aClass) {
        super(String.format("Не найден %s {id=%s}", aClass, resourceId));
    }
}
