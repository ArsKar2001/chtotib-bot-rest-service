package com.karmanchik.chtotib_bot_rest_service.parser;

import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j
@Component
public class WordParser {

    public static final Map<String, Integer> DAYS_OF_WEEK = Map.of(
            "Понедельник", 0,
            "Вторник", 1,
            "Среда", 2,
            "Четверг", 3,
            "Пятница", 4,
            "Суббота", 5,
            "Воскресенье", 6
    );

    public String wordFileAsText(InputStream stream) {
        try (FileInputStream stream_ = (FileInputStream) stream) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(stream_));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            stream_.close();
            return extractor.getText();
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException();
        }
    }

    public List<List<String>> textToPages(List<String> strings) {
        try {
            List<String> page = new ArrayList<>();
            List<List<String>> pages = new ArrayList<>();
            for (String string : strings) {
                if ("".equals(string)) {
                    List<String> finalPage1 = page;
                    page.removeIf(s -> finalPage1.indexOf(s) == 0 || finalPage1.indexOf(s) == 2);
                    pages.add(page);
                    page = new ArrayList<>();
                } else {
                    if (string.endsWith("\t") || string.startsWith("\t")) page.add(string);
                    else page.add(string.trim());
                }
            }
            List<String> finalPage = page;
            page.removeIf(s -> finalPage.indexOf(s) == 0 || finalPage.indexOf(s) == 2);
            pages.add(page);
            List<List<String>> splitPages = splitPages(pages);
            splitPages.removeIf(pg -> pg.get(0).split("\\s").length == 1);
            return currentPages(splitPages);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public JSONArray createTimetable(InputStream stream) {
        try {
            String text = wordFileAsText(stream);
            List<String> strings = Arrays.asList(text.split("\n"));
            List<List<String>> pages = textToPages(strings);
            JSONArray allGroups = new JSONArray();
            JSONObject group;
            JSONArray timetable;
            JSONObject lesson;
            for (List<String> page : pages) {
                group = new JSONObject();
                timetable = new JSONArray();

                group.put("group_name", page.get(0));
                page.remove(0);
                for (String s : page) {
                    lesson = new JSONObject();
                    String[] splitStr = s.split(";");
                    lesson.put("day_of_week", DAYS_OF_WEEK.get(splitStr[0]));
                    lesson.put("lesson_number", splitStr[1]);
                    lesson.put("discipline", splitStr[2]);
                    lesson.put("auditorium", splitStr[3]);
                    lesson.put("teacher", splitStr[4]);
                    lesson.put("week_type", splitStr[5]);
                    timetable.put(lesson);
                }
                group.put("timetable", timetable);
                allGroups.put(group);
            }
            log.debug("Create new JSON: " + allGroups.toString());
            return allGroups;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<List<String>> currentPages(List<List<String>> splitPages) {
        StringBuilder currentStr1;
        StringBuilder currentStr2;
        List<String> currentPage;
        List<List<String>> currentPages = new ArrayList<>();

        for (List<String> splitPage : splitPages) {
            currentPage = new ArrayList<>();
            for (String str : splitPage) {
                currentStr1 = new StringBuilder();
                currentStr2 = new StringBuilder();
                if (str.equals(splitPage.get(0))) {
                    currentStr1.append(str.split("\\s")[1]);
                } else {
                    String[] splitStr = str.split(";");
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
                    } else if (splitStr[1].contains("-")) {
                        String[] splitStr2 = splitStr.clone();
                        String[] ss = splitStr[1].split("-");
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
            }
            currentPages.add(currentPage);
        }
        return currentPages;
    }

    private List<List<String>> splitPages(List<List<String>> pages) {
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
                String[] arrStr = str.split("\\t", -10);
                for (int i = 0; i < arrStr.length; i++) {
                    if (!str.equals(page.get(0)) && !"".equals(arrStr[0])) {
                        dayOfWeek = arrStr[0];
                    }
                    if ("".equals(arrStr[i].trim()) && i != 0) arrStr[i] = "-";
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
}
