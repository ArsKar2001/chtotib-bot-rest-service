package com.karmanchik.chtotib_bot_rest_service.parser.validate;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;

import java.util.regex.Pattern;

public class ValidTeacherName {
    private static final Pattern PATTERN = Pattern.compile("[А-Я]?[А-Я]+\\s+[А-Я]\\.[А-Я]\\.");

    private ValidTeacherName() {
    }

    public static Pattern getPattern() {
        return PATTERN;
    }

    public static String getValidTeacherName(String teacher) throws StringReadException {
        return null;
    }
}
