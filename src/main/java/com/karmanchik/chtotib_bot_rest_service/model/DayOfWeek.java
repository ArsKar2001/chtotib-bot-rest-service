package com.karmanchik.chtotib_bot_rest_service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DayOfWeek {
    private static final Map<Integer, String> DAYS_OF_WEEK = Map.of(
            0, "Понедельник",
            1, "Вторник",
            2, "Среда",
            3, "Четверг",
            4, "Пятница",
            5, "Суббота",
            6, "Воскресенье"
    );

    public static Map<Integer, String> getAll() {
        return DAYS_OF_WEEK;
    }

    public static boolean containsKey(Object o) {
        return DAYS_OF_WEEK.containsKey(o);
    }

    public static boolean containsValue(Object o) {
        return DAYS_OF_WEEK.containsValue(o);
    }

    public static String get(Object o) {
        return DAYS_OF_WEEK.get(o);
    }

    public static List<Integer> getKeys() {
        return new ArrayList<>(DAYS_OF_WEEK.keySet());
    }

    public static List<String> getValues() {
        return new ArrayList<>(DAYS_OF_WEEK.values());
    }
}
