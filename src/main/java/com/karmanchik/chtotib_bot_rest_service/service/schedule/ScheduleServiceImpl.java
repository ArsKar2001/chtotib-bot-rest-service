package com.karmanchik.chtotib_bot_rest_service.service.schedule;

import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j
public class ScheduleServiceImpl implements ScheduleService {
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

    public ScheduleServiceImpl() {
    }

    @Override
    public Collection<List<String>> textToLists(String text) {
        final String rText = text.replace('\t', ';');
        final String[] sText = rText.split("\n");

        List<String> stringList = new LinkedList<>(Arrays.asList(sText));
        stringList.removeIf(String::isBlank);

        final var ll = createListLists(stringList);
        final var sll = splitListLists(ll);
        return correctingListLists(sll);
    }

    private Collection<List<String>> createListLists(List<String> stringList) {
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

    private Collection<List<String>> splitListLists(Collection<List<String>> listLists) {
        Collection<String> listRight = new LinkedList<>();
        Collection<String> listLeft = new LinkedList<>();
        Collection<List<String>> lls = new LinkedList<>();

        for (var list : listLists) {
            for (String str : list) {
                List<String> ls1 = new LinkedList<>();
                List<String> ls2 = new LinkedList<>();
                final String[] splitStr = str.split(";", -5);
                for (int i = 0; i < splitStr.length; i++) {
                    if (i < splitStr.length / 2)
                        ls1.add(splitStr[i].trim().isBlank() ? "-" : DAYS_OF_WEEK.get(splitStr[i].trim()).toString());
                    else ls2.add(splitStr[i].trim().isBlank() ? "-" : DAYS_OF_WEEK.get(splitStr[i].trim()).toString());
                }
                final String s1 = String.join(";", ls1).trim();
                final String s2 = String.join(";", ls2).trim();

                Collection<? extends String> c1 = splitString(s1);
                Collection<? extends String> c2 = splitString(s2);

                listLeft.addAll(c1);
                listLeft.addAll(c2);
            }
            lls.add(new LinkedList<>(listLeft));
            lls.add(new LinkedList<>(listRight));
            listLeft.clear();
            listRight.clear();
        }
        return lls;
    }

    private Collection<? extends String> splitString(String s1) {
        StringBuilder currentStr1 = new StringBuilder();
        StringBuilder currentStr2 = new StringBuilder();
        List<String> ls = new LinkedList<>();

        String[] splitStr = s1.split(";");
        String number = splitStr[2].trim();
        if (s1.contains("/")) {
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
            ls.add(currentStr2.toString());
        } else {
            currentStr1.append(s1).append(";").append("NONE");
        }
        ls.add(currentStr1.toString());
        return ls;
    }

    private String getSplit(String number) {
        Pattern pattern = Pattern.compile("[;,-/|]");
        Matcher matcher = pattern.matcher(number);
        return matcher.find() ?
                number.substring(matcher.start(), matcher.end()) : "";
    }

    private boolean isCorrectNumber(String s) {
        return (s.contains("-") || s.contains(",")) && !s.isBlank() && s.length() > 1;
    }

    private List<List<String>> correctingListLists(Collection<List<String>> sListLists) {
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
                    if (!strings[0].equals("-")) s2 = strings[0];
                    else {
                        str = str.substring(1);
                        str = s2 + str;
                    }
                    str = s1 + ";" + str;
                    ls.add(str);
                }
            }
            lls.add(new LinkedList<>(ls));
            ls.clear();
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

    @Override
    public JSONArray createScheduleAsJSON(String text) {
        JSONArray allGroups = new JSONArray();
        JSONObject group;
        JSONArray timetable;
        JSONObject lesson;

        var lists = this.textToLists(text);
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
                    } catch (RuntimeException e) {
                        throw new RuntimeException(String.format("%s; в строке \"%s\"", e, s));
                    }
                }
                group.put("timetable", timetable);
                allGroups.put(group);
            } catch (RuntimeException e) {
                throw new RuntimeException(e + "; в " + list.toString());
            }
        }
        log.debug("Create new JSON: " + allGroups.toString());
        return allGroups;
    }

    @Override
    public JSONArray createReplacementAsJSON(String text) {
        return null;
    }

    /*public List<List<String>> textToLists(List<String> strings) {
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
                } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<String>> currentPages(List<List<String>> splitPages) {
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

    public String getGroupName(String str) {
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
    }*/
}
