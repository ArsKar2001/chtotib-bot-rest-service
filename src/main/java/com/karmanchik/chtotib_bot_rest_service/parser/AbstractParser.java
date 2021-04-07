package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public abstract class AbstractParser {

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

    /**
     * Формирует данные в csv из исходного текста
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    public abstract List<String> textToCSV(String text) throws StringReadException;

    /**
     * Формирует данные в json из исходного текста
     * @param text Исходный текст из файла
     * @return массив json
     * @throws StringReadException Неверное чтение строки
     */
    public abstract JSONArray textToJSON(String text) throws StringReadException;

    public abstract List<List<List<String>>> parse() throws IOException, InvalidFormatException;
}
