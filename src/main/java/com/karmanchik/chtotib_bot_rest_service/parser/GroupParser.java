package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.model.DayOfWeek;
import com.karmanchik.chtotib_bot_rest_service.model.GroupName;
import com.karmanchik.chtotib_bot_rest_service.service.Word;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class GroupParser {
    private final InputStream stream;

    public GroupParser(InputStream stream) {
        this.stream = stream;
    }

    public String parse() throws StringReadException {
        JSONArray groups = new JSONArray();
        JSONObject group;
        JSONArray lessons;
        JSONObject lesson;

        final String text = Word.getText(stream);
        log.debug("!!!!!!!!! log debug: reading file.");
        final var lists = textToCSV(text);
        log.debug("!!!!!!!!! log debug: create lists - \"{}\"", Arrays.toString(lists.toArray()));

        for (var list : lists) {
            group = new JSONObject();
            lessons = new JSONArray();
            for (String s : list) {
                try {
                    lesson = new JSONObject();
                    String[] splitStr = s.split(";");
                    lesson.put("day_of_week", splitStr[1]);
                    lesson.put("number", splitStr[2]);
                    lesson.put("discipline", splitStr[3]);
                    lesson.put("auditorium", splitStr[4]);
                    lesson.put("teacher", splitStr[5]);
                    lesson.put("week_type", splitStr[6]);
                    lessons.put(lesson);
                } catch (Exception e) {
                    throw new StringReadException(s);
                }
            }
            String groupName = list.get(0).split(";")[0];
            group.put("group_name", groupName);
            group.put("lessons", lessons);
            groups.put(group);
        }
        log.debug("Create new JSON: " + groups.toString());
        return groups.toString();
    }

    public List<List<String>> textToCSV(String text) throws StringReadException {
        final String rText = text.replace('\t', ';');
        final String[] sText = rText.split("\n");

        List<String> stringList = new LinkedList<>(Arrays.asList(sText));
        stringList.removeIf(String::isBlank);

        final var ll = createListLists(stringList);
        final var sll = splitRightLeftListLists(ll);
        final var cll = correctingListLists(sll);
        return splitUpDownNoneListLists(cll);
    }

    private List<List<String>> createListLists(List<String> stringList) throws StringReadException {
        List<List<String>> listLists = new LinkedList<>();
        List<String> list = new LinkedList<>();

        Pattern pattern = Pattern.compile("/\\S\\.\\S\\.\\s.+?/");
        final String removeStr = "\u0424.\u0418.\u041E.";
        stringList.removeIf(s -> s.contains(removeStr));
        for (String str : stringList) {
            try {
                if (pattern.matcher(str).find()) {
                    listLists.add(new LinkedList<>(list));
                    list.clear();
                } else {
                    list.add(str);
                }
            } catch (Exception e) {
                throw new StringReadException(str);
            }
        }
        listLists.get(0).addAll(list);
        return listLists;
    }

    private List<List<String>> splitRightLeftListLists(List<List<String>> listLists) throws StringReadException {
        List<String> listRight = new LinkedList<>();
        List<String> listLeft = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();

        for (var list : listLists) {
            for (String str : list) {
                try {
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
                } catch (Exception e) {
                    throw new StringReadException(str, e);
                }
            }
            lls.add(new LinkedList<>(listLeft));
            lls.add(new LinkedList<>(listRight));
            listLeft.clear();
            listRight.clear();
        }
        return lls;
    }

    private List<List<String>> correctingListLists(List<List<String>> sListLists) throws StringReadException {
        List<String> ls = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();
        String s1 = "";
        String s2 = "";

        sListLists.removeIf(list -> list.get(0).split("\\s").length < 2);
        for (var list : sListLists) {
            for (String str : list) {
                Matcher matcher = GroupName.matcher(str);
                if (matcher.find()) {
                    String s = str.substring(matcher.start(), matcher.end());
                    s1 = GroupName.getValidGroupName(s);
                } else {
                    try {
                        final String[] strings = str.split(";", -5);
                        if (!strings[0].equals("-"))
                            s2 = DayOfWeek.containsKey(strings[0]) ? DayOfWeek.get(strings[0]) : strings[0];
                        str = str.substring(str.indexOf(';'));
                        str = s2 + str;
                        str = s1 + ";" + str;
                        ls.add(str);
                    } catch (Exception e) {
                        throw new StringReadException(str, e);
                    }
                }
            }
            lls.add(new LinkedList<>(ls));
            ls.clear();
        }
        return lls;
    }

    private List<List<String>> splitUpDownNoneListLists(List<List<String>> cll) throws StringReadException {
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
                    throw new StringReadException(str, e);
                }
            }
            lls.add(ls);
        }
        return lls;
    }
}