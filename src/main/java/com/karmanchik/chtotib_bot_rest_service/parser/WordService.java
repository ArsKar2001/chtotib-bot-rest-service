package com.karmanchik.chtotib_bot_rest_service.parser;

import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Log4j
public class WordService {

    public static final String REMOVE_ROW_1 = "\u0421\u041E\u0413\u041B\u0410\u0421\u041E\u0412\u0410\u041D\u041E";
    public static final String REMOVE_ROW_2 = "\u0414\u043D\u0438 \u043D\u0435\u0434\u0435\u043B\u0438";
    public static final char[] SPLIT_CHAR = {';', ',', '-', '|'};
    public static final Map<String, Integer> DAYS_OF_WEEK = Map.of(
            "Понедельник", 0,
            "Вторник", 1,
            "Среда", 2,
            "Четверг", 3,
            "Пятница", 4,
            "Суббота", 5,
            "Воскресенье", 6
    );
    private final InputStream stream;

    public WordService(InputStream stream) {
        this.stream = stream;
    }

    public String wordFileAsText() {
        try (InputStream stream = this.stream) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(stream));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            stream.close();
            String text = extractor.getText();
            return text;
        } catch (InvalidFormatException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public List<List<String>> textToLists(List<String> strings) {
        try {
            List<String> list = new LinkedList<>();
            List<List<String>> lists = new LinkedList<>();

            for (String string : strings) {
                try {
                    if (string.isBlank()) {
                        lists.add(new LinkedList<>(list));
                        list.clear();
                    }
                    list.add(string.trim());
                } catch (Exception e) {
                    throw new RuntimeException(e + "; в строке: " + string);
                }
            }
            lists.add(new LinkedList<>(list));

            lists.removeIf(page_ -> page_.isEmpty() || page_.size() < 5);
            lists.forEach(page_ -> {
                page_.removeIf(String::isBlank);
                page_.removeIf(s -> s.contains(REMOVE_ROW_1) || s.contains(REMOVE_ROW_2));
            });

            var splitPages = this.splitPages(lists);
            splitPages.removeIf(pg -> pg.get(0).split("\\s").length == 1);

            return this.currentPages(splitPages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray createTimetable() {
        JSONArray allGroups = new JSONArray();
        JSONObject group;
        JSONArray timetable;
        JSONObject lesson;

        var text = this.wordFileAsText().replace('\t', ';');
        log.debug(String.format("!!!!!!!!! log 1: read file - \"%s\"", text));
        var splitStrings = text.trim().split("\n");
        log.debug(String.format("!!!!!!!!! log 2: split text - \"%s\"", Arrays.toString(splitStrings)));
        var strings = new LinkedList<>(Arrays.asList(splitStrings));
        log.debug(String.format("!!!!!!!!! log 3: create LinkedList - \"%s\"", Arrays.toString(strings.toArray())));
        var lists = this.textToLists(strings);
        log.debug(String.format("!!!!!!!!! log 4: create lists - \"%s\"", Arrays.toString(lists.toArray())));

        for (var list : lists) {
            try {
                group = new JSONObject();
                timetable = new JSONArray();

                String group_name = list.get(0);
                log.debug(String.format("!!!!!!!!! log 4: create lists - \"%s\"", Arrays.toString(lists.toArray())));
                group.put("group_name", group_name);
                list.remove(0);
                for (String s : list) {
                    try {
                        lesson = new JSONObject();
                        String[] splitStr = s.split(";");
                        lesson.put("day_of_week", DAYS_OF_WEEK.get(splitStr[0]));
                        lesson.put("lesson_number", splitStr[1]);
                        lesson.put("discipline", splitStr[2]);
                        lesson.put("auditorium", splitStr[3]);
                        lesson.put("teacher", splitStr[4]);
                        lesson.put("week_type", splitStr[5]);
                        timetable.put(lesson);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("%s; в строке \"%s\"", e, s));
                    }
                }
                group.put("timetable", timetable);
                allGroups.put(group);
            } catch (Exception e) {
                throw new RuntimeException(e + "; в " + list.toString());
            }
        }
        log.debug("Create new JSON: " + allGroups.toString());
        return allGroups;
    }

    private List<List<String>> currentPages(List<List<String>> splitPages) {
        StringBuilder currentStr1;
        StringBuilder currentStr2;
        List<String> currentPage;
        List<List<String>> currentPages = new ArrayList<>();

        for (List<String> splitPage : splitPages) {
            currentPage = new ArrayList<>();
            for (String str : splitPage) {
                try {
                    currentStr1 = new StringBuilder();
                    currentStr2 = new StringBuilder();
                    if (str.equalsIgnoreCase(splitPage.get(0))) {
                        String groupName = this.getGroupName(str);
                        currentStr1.append(groupName);
                    } else {
                        String[] splitStr = str.split(";");
                        String number = splitStr[1].trim();
                        if (str.contains("/")) {
                            String[] splitStr2 = splitStr.clone();
                            for (int i = 0; i < splitStr.length; i++) {
                                if (splitStr[i].contains("/")) {
                                    String[] ss = splitStr[i].split("/", -5);
                                    splitStr[i] = ss[0].trim();
                                    splitStr2[i] = ss[1].trim();
                                }
                            }
                            currentStr1.append(
                                    String.join(";", splitStr)
                            ).append(";").append("UP");
                            currentStr2.append(
                                    String.join(";", splitStr2)
                            ).append(";").append("DOWN");
                            currentPage.add(currentStr2.toString());
                        } else if (isCorrectNumber(number)) {
                            String[] splitStr2 = splitStr.clone();
                            String split = getSplit(number);
                            String[] ss = number.split(split);

                            splitStr[1] = ss[0].trim();
                            splitStr2[1] = ss[1].trim();

                            currentStr1.append(
                                    String.join(";", splitStr)
                            ).append(";").append("NONE");
                            currentStr2.append(
                                    String.join(";", splitStr2)
                            ).append(";").append("NONE");
                            currentPage.add(currentStr2.toString());
                        } else {
                            currentStr1.append(str).append(";").append("NONE");
                        }
                    }
                    currentPage.add(currentStr1.toString());
                } catch (Exception e) {
                    throw new RuntimeException(String.format("%s; в строке \"%s\"", e, str));
                }
            }
            currentPages.add(currentPage);
        }
        return currentPages;
    }

    public List<List<String>> splitPages(List<List<String>> pages) {
        List<List<String>> splitPages = new ArrayList<>();
        List<String> pageRight;
        List<String> pageLeft;
        StringBuilder strRight;
        StringBuilder strLeft;
        String dayOfWeek = "";

        for (List<String> page : pages) {
            pageRight = new ArrayList<>();
            pageLeft = new ArrayList<>();
            for (String str : page) {
                strRight = new StringBuilder();
                strLeft = new StringBuilder();
                String[] arrStr = str.split(";", -10);
                for (int i = 0; i < arrStr.length; i++) {
                    if (!str.equalsIgnoreCase(page.get(0)) && !arrStr[0].isBlank()) {
                        dayOfWeek = arrStr[0];
                    }
                    if (arrStr[i].trim().isBlank() && i != 0) arrStr[i] = "-";
                    if (i < arrStr.length / 2) strRight.append(arrStr[i].trim()).append(";");
                    else strLeft.append(arrStr[i].trim()).append(";");
                }
                if (strLeft.toString().split(";")[0].equals("") || strRight.toString().split(";")[0].equals("")) {
                    strLeft.replace(0, 1, dayOfWeek);
                    strRight.replace(0, 0, dayOfWeek);
                }
                pageRight.add(strRight.substring(0, strRight.length() - 1));
                pageLeft.add(strLeft.substring(0, strLeft.length() - 1));
            }
            splitPages.add(pageRight);
            splitPages.add(pageLeft);
        }
        return splitPages;
    }

    private String getGroupName(String str) {
        var split = str.trim().split("\\s");
        String s = split[0].trim();
        List<String> stringList = new LinkedList<>(Arrays.asList(split));
        stringList.removeIf(String::isBlank);
        stringList.remove(s);
        return String.join("-", stringList);
    }

    private String getSplit(String numberStr) {
        char[] chars = numberStr.toCharArray();
        for (char aChar : chars)
            for (char c : SPLIT_CHAR)
                if (aChar == c) return String.valueOf(c);
        return "";
    }

    private boolean isCorrectNumber(String s) {
        return (s.contains("-") || s.contains(",")) && !s.isBlank() && s.length() > 1;
    }
}
