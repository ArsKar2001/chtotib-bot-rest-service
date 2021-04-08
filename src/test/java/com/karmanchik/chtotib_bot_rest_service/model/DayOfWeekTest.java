package com.karmanchik.chtotib_bot_rest_service.model;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DayOfWeekTest {
    @Test
    void testMonth() {
        Arrays.stream(Month.values())
                .map(month -> month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")))
                .forEach(System.out::println);
    }
}