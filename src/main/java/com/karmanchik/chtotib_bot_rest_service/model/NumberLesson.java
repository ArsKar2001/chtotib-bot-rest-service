package com.karmanchik.chtotib_bot_rest_service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NumberLesson {
    private static final Map<String, Integer> COURSES = Map.of(
            "I", 0,
            "II", 1,
            "III", 2,
            "IV", 3,
            "V", 4,
            "VI", 5,
            "VII", 6,
            "VIII", 7

    );

    private NumberLesson() {
    }

    public static Map<String, Integer> getAll() {
        return COURSES;
    }

    public static boolean containsKey(Object o) {
        return COURSES.containsKey(o);
    }

    public static boolean containsValue(Object o) {
        return COURSES.containsValue(o);
    }

    public static Integer get(Object o) {
        return COURSES.get(o);
    }

    public static List<String> getKeys() {
        return new ArrayList<>(COURSES.keySet());
    }

    public static List<Integer> getValues() {
        return new ArrayList<>(COURSES.values());
    }
}