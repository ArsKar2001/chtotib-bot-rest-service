package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.service.schedule.ScheduleServiceImpl;
import com.karmanchik.chtotib_bot_rest_service.service.word.WordServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordService1Test2 {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");
    public static final Map<String, String> DAYS_OF_WEEK = Map.of(
            "Понедельник", "0",
            "Вторник", "1",
            "Среда", "2",
            "Четверг", "3",
            "Пятница", "4",
            "Суббота", "5",
            "Воскресенье", "6"
    );

    WordServiceImpl wordService = new WordServiceImpl();
    ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();

    @Test
    void test_File1() {
        try (FileInputStream stream = new FileInputStream(FILE_1)) {
            final String text = wordService.getText(stream);
            final var json = scheduleService.createScheduleAsJSON(text);
            System.out.println(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File2() {
        try (FileInputStream stream = new FileInputStream(FILE_2)) {
            final String text = wordService.getText(stream);
            final var json = scheduleService.createScheduleAsJSON(text);
            System.out.println(json.toString());
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> textToLists(String text) {
        final String rText = text.replace('\t', ';');
        final String[] sText = rText.split("\n");

        List<String> stringList = new LinkedList<>(Arrays.asList(sText));
        stringList.removeIf(String::isBlank);

        final var ll = createListLists(stringList);
        final var sll = splitListLists(ll);
        final var cll = correctingListLists(sll);
        return splitUpDownNoneListLists(cll);
    }

    private List<List<String>> createListLists(List<String> stringList) {
        List<List<String>> listLists = new LinkedList<>();
        List<String> list = new LinkedList<>();

        Pattern pattern = Pattern.compile("/\\S\\.\\S\\.\\s.+?/");
        final String removeStr = "\u0424.\u0418.\u041E.";
        stringList.removeIf(s -> s.contains(removeStr));
        for (String str : stringList) {
            if (pattern.matcher(str).find()) {
                listLists.add(new LinkedList<>(list));
                list.clear();
            } else {
                list.add(str);
            }
        }
        listLists.get(0).addAll(list);
        return listLists;
    }

    private List<List<String>> splitListLists(List<List<String>> listLists) {
        List<String> listRight = new LinkedList<>();
        List<String> listLeft = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();

        for (var list : listLists) {
            for (String str : list) {
                List<String> ls1 = new LinkedList<>();
                List<String> ls2 = new LinkedList<>();
                final String[] splitStr = str.split(";", -5);
                for (int i = 0; i < splitStr.length; i++) {
                    if (i < splitStr.length / 2) ls1.add(splitStr[i].trim().isBlank() ? "-" : splitStr[i].trim());
                    else ls2.add(splitStr[i].trim().isBlank() ? "-" : splitStr[i].trim());
                }
                final String s1 = String.join(";", ls1).trim();
                final String s2 = String.join(";", ls2).trim();

                listLeft.add(s1);
                listRight.add(s2);
            }
            lls.add(new LinkedList<>(listLeft));
            lls.add(new LinkedList<>(listRight));
            listLeft.clear();
            listRight.clear();
        }
        return lls;
    }

    private List<List<String>> correctingListLists(List<List<String>> sListLists) {
        List<String> ls = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();
        Pattern pt = Pattern.compile("([А-Я]|[а-я])+(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\d{1,2}(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\S{1,2}");
        String s1 = "";
        String s2 = "";

        sListLists.removeIf(list -> list.get(0).split("\\s").length < 2);
        for (var list : sListLists) {
            for (String str : list) {
                Matcher matcher = pt.matcher(str);
                if (matcher.find()) {
                    String substring = str.substring(matcher.start(), matcher.end());
                    s1 = this.getValidGroupName(substring);
                } else {
                    final String[] strings = str.split(";", -5);
                    if (strings.length > 5) throw new StringReadException(str, ";", strings.length);
                    if (!strings[0].equals("-"))
                        s2 = DAYS_OF_WEEK.containsKey(strings[0]) ? DAYS_OF_WEEK.get(strings[0]) : strings[0];
                    str = str.substring(str.indexOf(';'));
                    str = s2 + str;
                    str = s1 + ";" + str;
                    ls.add(str);
                }
            }
            lls.add(new LinkedList<>(ls));
            ls.clear();
        }
        return lls;
    }

    private List<List<String>> splitUpDownNoneListLists(List<List<String>> cll) {
//        Pattern pt1 = Pattern.compile("[IV]+([,/|-]|\\s+|[,/|-]\\s+|\\s+[,/|-]|\\s+[,/|-]\\s+)[IV]+");
//        Pattern pt2 = Pattern.compile("[,/|-]+");

        StringBuilder currentStr1;
        StringBuilder currentStr2;
        List<String> ls;
        List<List<String>> lls = new ArrayList<>();

        for (List<String> splitPage : cll) {
            ls = new ArrayList<>();
            for (String str : splitPage) {
                try {
                    currentStr1 = new StringBuilder();
                    currentStr2 = new StringBuilder();
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
                        ls.add(currentStr2.toString());
                    } else {
                        currentStr1.append(str).append(";").append("NONE");
                    }
                    ls.add(currentStr1.toString());
                } catch (Exception e) {
                    throw new RuntimeException(String.format("%s; в строке \"%s\"", e, str));
                }
            }
            lls.add(ls);
        }
        return lls;
    }

    private String getValidGroupName(String s) {
        List<String> list = new LinkedList<>();
        String s1 = s.replace('-', ' ');
        Pattern pt = Pattern.compile("((\\d+([а-я]|))|([А-Я]|[а-я])+)");
        Matcher mt = pt.matcher(s1);

        while (mt.find()) {
            String s2 = s1.substring(mt.start(), mt.end());
            list.add(s2);
        }
        return String.join("-", list);
    }
}