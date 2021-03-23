package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class TimetableParser extends AbstractBaseParser {

    private List<String> list;

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<? extends String> textToCSV(String text) throws StringReadException {
        String rText = text.replace('\t', ';');
        String[] sText = rText.split("\n");
        Pattern groupName = ValidGroupName.getGroupName();

        List<String> list = new LinkedList<>(Arrays.asList(sText));
        list.removeIf(String::isBlank);
        list.remove(0);

        this.list = list;

        listToRemoveTitleRows();
        splitListsOfLists();
        listsCorrectionOne();
        listsCorrectionTwo();
//        // Разделяет листы на правую и левую группы
//        final List<List<String>> splitLists = splitListsOfLists(listLists);
//        // Удаление пустых листов
//        splitLists.removeIf(strings -> !groupName
//                        .matcher(strings.get(0))
//                        .find());
//
//        // Корректирует строки дней недели в листах
//        final List<List<String>> one = listsCorrectionOne(splitLists);
//
//        List<String> two = listsCorrectionTwo(one);

        return this.list;
    }

    private void listToRemoveTitleRows() {
        List<String> nList = new LinkedList<>();
        List<String> tempList = List.copyOf(list);
        list.clear();
        Pattern pattern = Pattern.compile("^\\D+/$");
        tempList.forEach(s -> {
            if (pattern.matcher(s).find()) {
                nList.remove(1);
                this.list.addAll(List.copyOf(nList));
                nList.clear();
            } else nList.add(s);
        });
        nList.remove(1);
        this.list.addAll(List.copyOf(nList));
    }

    private void splitListsOfLists() throws StringReadException {
        List<String> tempList = List.copyOf(list);
        list.clear();

        List<String> rightList = new LinkedList<>();
        List<String> leftList = new LinkedList<>();
        List<String> leftListStr = new LinkedList<>();
        List<String> rightListStr = new LinkedList<>();

        for (String s : tempList) {
            leftListStr.clear();
            rightListStr.clear();
            String[] arrStr = s.trim().split(CSV_SPLIT, SPLIT_LIMIT);
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
            } else
                throw new StringReadException(s, arrStr.length);
        }
        this.list.addAll(List.copyOf(rightList));
        this.list.addAll(List.copyOf(leftList));
    }

    private void listsCorrectionOne() {
        List<String> tempList = List.copyOf(list);
        this.list.clear();
        AtomicReference<String> s2 = new AtomicReference<>("");

        tempList.forEach(s -> {
            String[] arrStr = s.split(CSV_SPLIT, SPLIT_LIMIT);
            if (arrStr.length > MIN_COLUMN_SIZE) {
                final String s1 = arrStr[0];
                if (!Objects.equals(s1, DEFAULT_VALUE)) {
                    s2.set(s1);
                    list.add(s);
                } else {
                    String s3 = s2.get() + s.substring(s.indexOf(CSV_SPLIT));
                    list.add(s3);
                }
            } else {
                list.add(s);
            }
        });
    }

    private void listsCorrectionTwo() {
        List<String> tempList = new LinkedList<>(list);
        this.list.clear();
        boolean removed = false;

        for (String s : tempList) {
            String[] arrStr = s.trim().split(CSV_SPLIT, SPLIT_LIMIT);
            removed = arrStr.length < MIN_COLUMN_SIZE;
            if (removed) tempList.remove(s);
        }
    }

    private List<String> listsCorrectionThree(List<String> two) {
        Pattern pattern = ValidGroupName.getGroupName();
        AtomicReference<String> s2 = new AtomicReference<>("");
        List<String> tempList = List.copyOf(list);
        this.list.clear();

        tempList.forEach(s -> {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find())
                s2.set(s.substring(matcher.start(), matcher.end()));
            else {
                s = s2.get() + CSV_SPLIT + s;
                this.list.add(s);
            }
        });
    }
}
