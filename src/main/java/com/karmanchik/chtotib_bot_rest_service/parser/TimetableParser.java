package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Log4j2
public class TimetableParser {

    /**
     * Максимальное кол-личество столбцов сдвоенное таблицы на странице Расписания Word-Документа
     */
    public static final int MAX_COLUMN_SIZE = 5;
    public static final int MIN_COLUMN_SIZE = 2;
    public static final int SPLIT_LIMIT = -10;
    public static final String DEFAULT_VALUE = "-";
    public static final String CSV_SPLIT = ";";

    /**
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     * Формирует данные в csv из исходного текста
     */
    public List<List<String>> textToCSV(String text) throws StringReadException {
        String rText = text.replace('\t', ';');
        String[] sText = rText.split("\n");

        List<String> list = new LinkedList<>(Arrays.asList(sText));
        list.removeIf(String::isBlank);
        list.remove(0);

        // Разделяет лист строк на листы по левой/правой группам
        final List<List<String>> listLists = listToListLists(list);
        // Разделяет листы на правую и левую группы
        final List<List<String>> splitLists = splitListsOfLists(listLists);
        // Корректирует строки в листах
        final List<List<String>> listsCorrection = listsCorrection(splitLists);

        return listsCorrection;
    }

    private List<List<String>> listToListLists(List<String> stringList) throws StringReadException {
        List<List<String>> lists = new LinkedList<>();
        List<String> list = new LinkedList<>();
        Pattern pattern = Pattern.compile("^\\D+/$");

        stringList.forEach(s -> {
            if (pattern.matcher(s).find()) {
                list.remove(1);
                lists.add(new LinkedList<>(list));
                list.clear();
            } else {
                list.add(s);
            }
        });
        list.remove(1);
        lists.add(new LinkedList<>(list));

        return lists;
    }

    private List<List<String>> splitListsOfLists(List<List<String>> lists) throws StringReadException {
        List<List<String>> newLists = new LinkedList<>();
        List<String> rightList = new LinkedList<>();
        List<String> leftList = new LinkedList<>();
        List<String> leftListStr = new LinkedList<>();
        List<String> rightListStr = new LinkedList<>();

        for (List<String> list : lists) {
            leftList.clear();
            rightList.clear();
            for (String s : list) {
                leftListStr.clear();
                rightListStr.clear();
                final String[] arrStr = s.trim().split(CSV_SPLIT, SPLIT_LIMIT);
                if (arrStr.length <= MAX_COLUMN_SIZE * 2 && arrStr.length >= MIN_COLUMN_SIZE) {
                    for (int i = 0; i < arrStr.length; i++) {
                        arrStr[i] = arrStr[i].trim().isBlank() ? DEFAULT_VALUE : arrStr[i];
                        if (i < arrStr.length / 2) {
                            leftListStr.add(arrStr[i].trim());
                        } else {
                            rightListStr.add(arrStr[i].trim());
                        }
                    }
                    rightList.add(
                            String.join(CSV_SPLIT, rightListStr)
                    );
                    leftList.add(
                            String.join(CSV_SPLIT, leftListStr)
                    );
                } else {
                    throw new StringReadException(s, arrStr.length);
                }
            }
            newLists.add(new LinkedList<>(rightList));
            newLists.add(new LinkedList<>(leftList));
        }
        return newLists;
    }

    private List<List<String>> listsCorrection(List<List<String>> splitListsOfLists) {
        List<List<String>> lists = new LinkedList<>();
        List<String> list = new LinkedList<>();
        String s2 = "";
        String s3;

        for (List<String> ll : splitListsOfLists) {
            list.clear();
            for (String s : ll) {
                final String[] arrStr = s.split(CSV_SPLIT);
                if (arrStr.length > MIN_COLUMN_SIZE) {
                    final String s1 = arrStr[0];
                    if (!Objects.equals(s1, DEFAULT_VALUE)) {
                        s2 = s1;
                        list.add(s);
                    } else {
                        s3 = s2 + s.substring(s.indexOf(CSV_SPLIT));
                        list.add(s3);
                    }
                } else {
                    list.add(s);
                }
            }
            lists.add(new LinkedList<>(list));
        }
        return lists;
    }
}
