package com.karmanchik.chtotib_bot_rest_service.parser.sequence;

public class Sequence {
    public static final int CSV_COLUMN_SIZE = 7;
    public static final int MAX_COLUMN_SIZE = 4;
    public static final int MIN_COLUMN_SIZE = 2;
    public static final int SPLIT_LIMIT = -10;
    public static final String DEFAULT_VALUE = "-";
    public static final String UP_DOWN_SPLIT = "/";
    public static final String CSV_SPLIT = ";";
    public static final String DEFAULT_SPLIT = ",";
    public static final String SPLIT_GROUP_ITEM = "║";
    public static final String LINE_SPLIT = "\n";
    public static final char NEW_CHAR = ';';
    public static final char OLD_CHAR = '\t';

    private Sequence() {
    }
}
