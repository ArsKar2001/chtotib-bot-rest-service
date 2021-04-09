package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractParser {

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

    public abstract List<List<String>> parse(InputStream stream) throws IOException, InvalidFormatException;
}
