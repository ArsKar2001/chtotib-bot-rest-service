package com.karmanchik.chtotib_bot_rest_service.model;

public enum WeekType {
    NONE(""),
    UP("верхняя"),
    DOWN("нижняя");

    private final String value;

    WeekType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WeekType{" +
                "value='" + value + '\'' +
                '}';
    }
}
