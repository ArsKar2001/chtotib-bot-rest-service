package com.karmanchik.chtotib_bot_rest_service.parser.validate;

import java.util.regex.Pattern;

public class ValidText {
    private final static Pattern PATTERN_REPLACE_SYMBOL = Pattern.compile("(\\|{2})");
    private final static Pattern PATTERN_CSV_STR = Pattern.compile("^([А-Яа-я]+|-);");

    private ValidText() {
    }

    public static Pattern getPatternCsvStr() {
        return PATTERN_CSV_STR;
    }

    public static Pattern getPatternReplaceSymbol() {
        return PATTERN_REPLACE_SYMBOL;
    }
}
