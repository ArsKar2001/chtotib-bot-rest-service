package com.karmanchik.chtotib_bot_rest_service.model;

public enum Role {
    NONE(""),
    STUDENT("студент"),
    TEACHER("педагог");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
