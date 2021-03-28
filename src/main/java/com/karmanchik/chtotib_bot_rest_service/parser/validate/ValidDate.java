package com.karmanchik.chtotib_bot_rest_service.parser.validate;

import com.karmanchik.chtotib_bot_rest_service.model.Month;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class ValidDate {
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}\\s?+" +
            "(\u0434\u0435\u043A\u0430\u0431\u0440\u044F" +
            "|\u044F\u043D\u0432\u0430\u0440\u044F" +
            "|\u0444\u0435\u0432\u0440\u0430\u043B\u044F" +
            "|\u043C\u0430\u0440\u0442\u0430" +
            "|\u0430\u043F\u0440\u0435\u043B\u044F" +
            "|\u043C\u0430\u044F" +
            "|\u0438\u044E\u043D\u044F" +
            "|\u0438\u044E\u043B\u044F" +
            "|\u0430\u0432\u0433\u0443\u0441\u0442\u0430" +
            "|\u0441\u0435\u043D\u0442\u044F\u0431\u0440\u044F" +
            "|\u043E\u043A\u0442\u044F\u0431\u0440\u044F" +
            "|\u043D\u043E\u044F\u0431\u0440\u044F)");

    private ValidDate() {}

    public static Pattern getDatePattern() {
        return DATE_PATTERN;
    }

    public static LocalDate strToDate(String s1) {
        String s2 = s1 + " " + Year.now().getValue();
        String[] strings = s2.split("\\s");
        strings[1] = Month.isMonth(strings[1]) ?
                String.valueOf(Month.getNumberMonth(strings[1])) : "0";
        String s3 = String.join(" ", strings);
        return LocalDate.parse(s3, DateTimeFormatter.ofPattern("dd M yyyy"));
    }
}
