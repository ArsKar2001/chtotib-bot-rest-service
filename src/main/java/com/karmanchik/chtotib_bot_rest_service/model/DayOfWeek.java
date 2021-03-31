package com.karmanchik.chtotib_bot_rest_service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DayOfWeek {
    private static final Map<String, Integer> DAYS_OF_WEEK = Map.of(
            "Понедельник", 1,
            "Вторник", 2,
            "Среда", 3,
            "Четверг", 4,
            "Пятница", 5,
            "Суббота", 6,
            "Воскресенье", 7
    );

    private DayOfWeek() {
    }

    public static boolean containsKey(String s) {
        return DAYS_OF_WEEK.containsKey(s);
    }

    public static boolean containsValue(Integer value) {
        return DAYS_OF_WEEK.containsValue(value);
    }

    public static Integer get(String s) {
        return DAYS_OF_WEEK.get(s);
    }

    public static List<String> getKeys() {
        return new ArrayList<>(DAYS_OF_WEEK.keySet());
    }

    public static List<Integer> getValues() {
        return new ArrayList<>(DAYS_OF_WEEK.values());
    }
}
