package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import com.karmanchik.chtotib_bot_rest_service.service.Word;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class TimetableParser {
    private final InputStream stream;

    public TimetableParser(InputStream stream) {
        this.stream = stream;
    }

/*    public String parse() throws StringReadException {
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
            String groupName = list.get(0).split(";")[0];

            for (String s : list) {
                try {
                    lesson = new JSONObject();
                    String[] splitStr = s.split(";");
                    int dayOfWeek = Integer.parseInt(splitStr[1]);
                    String validTeacherName = validTeacherName(splitStr[5]);

                    lesson.put("day_of_week", dayOfWeek);
                    lesson.put("number", splitStr[2]);
                    lesson.put("discipline", splitStr[3]);
                    lesson.put("auditorium", splitStr[4]);
                    lesson.put("teacher", validTeacherName);
                    lesson.put("week_type", splitStr[6]);

                    lessons.put(lesson);
                } catch (Exception e) {
                    throw new StringReadException(s);
                }
            }
            group.put("group_name", groupName);
            group.put("lessons", lessons);
            groups.put(group);
        }
        log.debug("Create new JSON: " + groups.toString());
        return groups.toString();
    }*/

    public List<?> createTimetableForGroup() throws StringReadException {
        String text = Word.getText(stream);
        var list = textToCSV(text);
        list.forEach(strings -> strings.forEach(System.out::println));
        return null;
    }

    public List<? extends List<? extends String>> textToCSV(String text) throws StringReadException {
        final String rText = text.replace('\t', ';');
        final String[] sText = rText.split("\n");

        List<String> stringList = new LinkedList<>(Arrays.asList(sText));
        stringList.removeIf(String::isBlank);

        final var ll = createListLists(stringList);
        final var sll = splitRightLeftListLists(ll);
        final var cll = correctingListLists(sll);
        return splitUpDownNoneListLists(cll);
    }

    private List<? extends List<? extends String>> createListLists(List<? extends String> stringList) throws StringReadException {
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

    private List<? extends List<? extends String>> splitRightLeftListLists(List<? extends List<? extends String>> listLists) throws StringReadException {
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

    private List<? extends List<? extends String>> correctingListLists(List<? extends List<? extends String>> sListLists) throws StringReadException {
        List<String> ls = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();
        String s1 = "";
        String s2 = "";
        Pattern ptGroupName = ValidGroupName.getPtGroupName();

        sListLists.removeIf(list -> list.get(0).split("\\s").length < 2);
        for (var list : sListLists) {
            for (String str : list) {
                try {
                    Matcher matcher = ptGroupName.matcher(str);
                    String[] strings = str.split(";");
                    if (matcher.find()) {
                        String s = str.substring(matcher.start(), matcher.end());
                        s1 = ValidGroupName.getValidGroupName(s);
                    } else {
                        String dayName = strings[0];
                        if (!Objects.equals(dayName, "-")) s2 = dayName;
                        else strings[0] = s2;
                        String s = String.join(";", strings);
                        str = s1 + ";" + s;
                        ls.add(str);
                    }
                } catch (Exception e) {
                    throw new StringReadException(str, e);
                }
            }
            lls.add(new LinkedList<>(ls));
            ls.clear();
        }
        return lls;
    }

    private List<? extends List<? extends String>> splitUpDownNoneListLists(List<? extends List<? extends String>> cll) throws StringReadException {
        StringBuilder currentStr1;
        StringBuilder currentStr2;
        List<String> ls;
        List<List<String>> lls = new ArrayList<>();

        for (var splitPage : cll) {
            ls = new ArrayList<>();
            for (String str : splitPage) {
                try {
                    currentStr1 = new StringBuilder();
                    currentStr2 = new StringBuilder();
                    String[] arrStr = str.split(";");
                    if (str.contains("/")) {
                        String[] arrStrClone = arrStr.clone();
                        for (int i = 0; i < arrStr.length; i++) {
                            if (arrStr[i].contains("/")) {
                                String[] ss = arrStr[i].split("/", -5);
                                arrStr[i] = ss[0].trim();
                                arrStrClone[i] = ss[1].trim();
                            }
                        }
                        currentStr1.append(
                                String.join(";", arrStr)
                        ).append(";").append("UP");
                        currentStr2.append(
                                String.join(";", arrStrClone)
                        ).append(";").append("DOWN");
                        ls.add(currentStr2.toString());
                    } else if (str.contains(",")){
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
