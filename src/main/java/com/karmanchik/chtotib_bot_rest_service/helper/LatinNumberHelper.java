package com.karmanchik.chtotib_bot_rest_service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LatinNumberHelper {
    private static final Map<String, Integer> NUMBER = Map.of(
            "I", 1,
            "II", 2,
            "III", 3,
            "IV", 4,
            "V", 5,
            "VI", 6,
            "VII", 7,
            "VIII", 8,
            "IX", 9,
            "X", 10
    );

    private LatinNumberHelper() {
    }

    public static Map<String, Integer> getAll() {
        return NUMBER;
    }

    public static boolean containsKey(String s) {
        return NUMBER.containsKey(s);
    }

    public static boolean containsValue(Integer integer) {
        return NUMBER.containsValue(integer);
    }

    public static Integer get(String s) {
        return NUMBER.get(s);
    }

    public static List<String> getKeys() {
        return new ArrayList<>(NUMBER.keySet());
    }

    public static List<Integer> getValues() {
        return new ArrayList<>(NUMBER.values());
    }
}