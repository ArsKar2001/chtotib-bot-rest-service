package com.karmanchik.chtotib_bot_rest_service.rest.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super(String.format("Не найден User {id=%s}", userId));
    }
}
