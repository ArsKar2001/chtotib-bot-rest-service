package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import org.json.JSONArray;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestParser extends AbstractParser {

    private static final List<String> list = new LinkedList<>();

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<String> textToCSV(String text) throws StringReadException {
        return textToList(text);
    }

    private List<String> textToList(String text) {
        List<String> csv = new LinkedList<>();
        List<String> ll = new LinkedList<>();
        List<String> rl = new LinkedList<>();
        char oldChar = '\t';
        char newChar = ';';
        Pattern ptReplace = Pattern.compile("(\\|{2})");
        Pattern ptGroup = Pattern.compile("[А-Яа-я]+(\\s?+-\\s?+|\\s?+)\\d{2}(\\s?+-\\s?+)\\d([а-я]?|)");
        Pattern ptLesson = Pattern.compile("^([А-Яа-я]+|);");

        String rText = text.replace(oldChar, newChar);
        rText = ptReplace.matcher(rText).replaceAll(SPLIT_GROUP_ITEM);

        Arrays.stream(rText.split(LINE_SPLIT))
                .map(String::trim)
                .dropWhile(String::isBlank)
                .filter(s -> ptGroup.matcher(s).find() || ptLesson.matcher(s).find())
                .map(s -> s.split(CSV_SPLIT, SPLIT_LIMIT))
                .map(ss -> Arrays.stream(ss)
                        .map(String::trim)
                        .map(s1 -> s1.isBlank() ? DEFAULT_VALUE : s1)
                        .collect(Collectors.toList()))
                .forEach(l -> {
                    int j = l.size() / 2;
                    ll.add(String.join(CSV_SPLIT, l.subList(0, j)));
                    rl.add(String.join(CSV_SPLIT, l.subList(j, l.size())));
                });

        csv.addAll(ll);
        csv.addAll(rl);

        return csv;
    }

    /**
     * Формирует данные в json из исходного текста
     *
     * @param text Исходный текст из файла
     * @return массив json
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public JSONArray textToJSON(String text) throws StringReadException {
        return null;
    }
}
