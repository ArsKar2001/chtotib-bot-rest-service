package com.karmanchik.chtotib_bot_rest_service.helper;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.regex.Pattern;

public class MonthHelper {
//    private static final Pattern DAY_MONTHS = new

    public static String getRusMonthName(Month month, TextStyle style) {
        return month.getDisplayName(style, Locale.forLanguageTag("ru"));
    }
}
