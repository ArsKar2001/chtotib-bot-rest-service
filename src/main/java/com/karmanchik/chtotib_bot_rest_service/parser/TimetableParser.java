package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.model.DayOfWeek;
import com.karmanchik.chtotib_bot_rest_service.model.NumberLesson;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidTeacherName;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class TimetableParser extends AbstractBaseParser {

    private final List<List<String>> lists = new LinkedList<>();

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<? extends String> textToCSV(String text) throws StringReadException {
        List<String> csv = new ArrayList<>();
        this.textToListLists(text);
        lists.forEach(csv::addAll);
        return csv;
    }

    @Override
    public JSONArray textToJSON(String text) throws StringReadException {
        JSONArray array = new JSONArray();
        JSONObject object;

        this.textToListLists(text);

        for (List<String> listCSV : lists) {
            for (String s : listCSV) {
                String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
                if (strings.length == CSV_COLUMN_SIZE) {
                    object = new JSONObject();
                    String groupName = strings[0];
                    String dayOfWeek = strings[1];
                    String pairNumber = strings[2];
                    String discipline = strings[3];
                    String auditorium = strings[4];
                    String teacherName = strings[5];
                    String weekType = strings[6];

                    int day = Integer.parseInt(dayOfWeek);
                    int pair = Integer.parseInt(pairNumber);
                    WeekType week = WeekType.valueOf(weekType);

                    object.put("group_name", groupName);
                    object.put("day", day);
                    object.put("pair", pair);
                    object.put("discipline", discipline);
                    object.put("auditorium", auditorium);
                    object.put("teacher_name", teacherName);
                    object.put("week", week);
                    array.put(object);
                } else
                    throw new StringReadException(s, strings.length);
            }
        }
        return array;
    }

    private void textToListLists(String text) throws StringReadException {
        String cText = textCorrection(text);
        textToListOfLists(cText);
        splitListsOfLists();
        listsOfListsCorrection();
        splitStringToListOfLists();
        splitStringByColumnAndSplitToListOfLists(SPLIT_GROUP_ITEM, 5, 4, 3);
        splitStringByColumnAndSplitToListOfLists(DEFAULT_SPLIT, 5, 4);
        splitStringByColumnAndSplitToListOfLists(DEFAULT_SPLIT, 2);
        listCorrectionAndValidate();
    }


    private String textCorrection(String text) {
        char newChar = ';';
        char oldChar = '\t';
        String rText = text.replace(oldChar, newChar);

        Pattern pattern = Pattern.compile("(\\|{2})");
        Matcher matcher = pattern.matcher(rText);
        return matcher.replaceAll(SPLIT_GROUP_ITEM);
    }

    private void textToListOfLists(String text) {
        String split = "\n";
        String[] array = text.split(split);

        List<String> tempList = new LinkedList<>();
        for (String s : array) {
            if (s.trim().isBlank()) {
                tempList.removeIf(String::isBlank);
                lists.add(new LinkedList<>(tempList));
                tempList.clear();
            } else
                tempList.add(s.trim());
        }
        lists.removeIf(List::isEmpty);
    }

    private void splitListsOfLists() throws StringReadException {
        List<List<String>> tll = new LinkedList<>(this.lists);
        lists.clear();

        List<String> rightList = new LinkedList<>();
        List<String> leftList = new LinkedList<>();
        List<String> leftListStr = new LinkedList<>();
        List<String> rightListStr = new LinkedList<>();

        for (List<String> list : tll) {
            leftList.clear();
            rightList.clear();
            for (String s : list) {
                leftListStr.clear();
                rightListStr.clear();
                String[] arrStr = s.trim().split(CSV_SPLIT, SPLIT_LIMIT);
                if (!s.equals(list.get(0))) {
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
            }
            this.lists.add(new LinkedList<>(rightList));
            this.lists.add(new LinkedList<>(leftList));
        }
    }

    private void listsOfListsCorrection() {
        List<List<String>> tLL = new LinkedList<>(this.lists);
        List<String> tL = new LinkedList<>();
        lists.clear();
        Matcher mt1;
        Matcher mt2;
        String s1 = "";
        String s3;

        Pattern ptLesson
                = Pattern.compile("^([А-Яа-я]+|-);");
        Pattern ptGroup = ValidGroupName.getPatternGroupName();

        for (List<String> list : tLL) {
            final String titleList = list.get(0);
            mt1 = ptGroup.matcher(titleList);
            if (mt1.find()) {
                s3 = titleList.substring(mt1.start(), mt1.end());
                for (String s : list) {
                    mt2 = ptLesson.matcher(s);
                    if (mt2.find()) {
                        final String[] arrStr = s.split(CSV_SPLIT, SPLIT_LIMIT);
                        final String s2 = arrStr[0];
                        if (!s2.equals(DEFAULT_VALUE)) {
                            s1 = s2;
                            s = s3 + CSV_SPLIT + s;
                        } else {
                            String s4 = s.substring(s.indexOf(CSV_SPLIT));
                            s = s3 + CSV_SPLIT + s1 + s4;
                        }
                        tL.add(s);
                    }
                }
            }
            this.lists.add(new LinkedList<>(tL));
            tL.clear();
        }
        lists.forEach(lists -> lists.removeIf(s -> {
            String[] strings = s.split(CSV_SPLIT);
            String numberLesson = strings[2];
            return numberLesson.equals(DEFAULT_VALUE);
        }));
    }

    private void splitStringToListOfLists() {
        List<List<String>> tLL = new LinkedList<>(this.lists);
        List<String> tl = new LinkedList<>();
        List<String> rtl = new LinkedList<>();
        List<String> ltl = new LinkedList<>();
        lists.clear();

        StringBuilder rsb;
        StringBuilder lsb;

        for (List<String> list : tLL) {
            tl.clear();
            for (String s : list) {
                rtl.clear();
                ltl.clear();
                rsb = new StringBuilder();
                lsb = new StringBuilder();
                final String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
                if (s.contains(UP_DOWN_SPLIT)) {
                    for (String string : strings) {
                        if (string.contains(UP_DOWN_SPLIT)) {
                            final String[] upDown = string.split(UP_DOWN_SPLIT, SPLIT_LIMIT);
                            rtl.add(upDown[0].trim().isBlank() ? DEFAULT_VALUE : upDown[0].trim());
                            ltl.add(upDown[1].trim().isBlank() ? DEFAULT_VALUE : upDown[1].trim());
                        } else {
                            rtl.add(string.trim());
                            ltl.add(string.trim());
                        }
                    }
                    rsb.append(
                            String.join(CSV_SPLIT, rtl)
                    ).append(CSV_SPLIT).append(WeekType.UP);
                    lsb.append(
                            String.join(CSV_SPLIT, ltl)
                    ).append(CSV_SPLIT).append(WeekType.DOWN);
                    tl.add(rsb.toString());
                } else
                    lsb.append(s).append(CSV_SPLIT).append(WeekType.NONE);
                tl.add(lsb.toString());
            }
            lists.add(new LinkedList<>(tl));
        }
    }

    private void splitStringByColumnAndSplitToListOfLists(String split, Integer... column) throws StringReadException {
        List<List<String>> tLL = new LinkedList<>(this.lists);
        List<Integer> columns = Arrays.asList(column);

        List<String> tl = new LinkedList<>();
        List<String> rtl = new LinkedList<>();
        List<String> ltl = new LinkedList<>();
        lists.clear();

        StringBuilder rsb;
        StringBuilder lsb;

        for (List<String> list : tLL) {
            tl.clear();
            for (String s : list) {
                rtl.clear();
                ltl.clear();
                rsb = new StringBuilder();
                lsb = new StringBuilder();
                final String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
                if (s.contains(split)) {
                    for (int i = 0; i < strings.length; i++) {
                        try {
                            if (columns.contains(i) && strings[i].contains(split)) {
                                final String[] upDown = strings[i].split(split, SPLIT_LIMIT);
                                rtl.add(upDown[0].trim().isBlank() ? DEFAULT_VALUE : upDown[0].trim());
                                ltl.add(upDown[1].trim().isBlank() ? DEFAULT_VALUE : upDown[1].trim());
                            } else {
                                rtl.add(strings[i].trim());
                                ltl.add(strings[i].trim());
                            }
                        } catch (Exception e) {
                            throw new StringReadException(s, strings[i], e);
                        }
                    }
                    rsb.append(
                            String.join(CSV_SPLIT, rtl)
                    );
                    lsb.append(
                            String.join(CSV_SPLIT, ltl)
                    );
                    tl.add(rsb.toString());
                } else
                    lsb.append(s);
                tl.add(lsb.toString());
            }
            lists.add(new LinkedList<>(tl));
        }
    }

    private void listCorrectionAndValidate() throws StringReadException {
        List<List<String>> tLL = new LinkedList<>(this.lists);
        List<String> tl = new LinkedList<>();
        lists.clear();

        for (List<String> list : tLL) {
            for (String s : list) {
                String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
                String groupName = strings[0];
                String dayOfWeek = strings[1];
                String numberLesson = strings[2];
                String teacher = strings[5];

                if (ValidGroupName.isGroupName(groupName))
                    strings[0] = ValidGroupName.getValidGroupName(groupName);
                else
                    throw new StringReadException(s, groupName, "АРХ-18-1 или ЗИО-18-2к");

                if (!teacher.equals(DEFAULT_VALUE))
                    if (ValidTeacherName.isTeacher(teacher))
                        strings[5] = teacher;
                    else
                        throw new StringReadException(s, teacher, "Иванов И.И.");

                if (DayOfWeek.containsKey(dayOfWeek))
                    strings[1] = DayOfWeek.get(dayOfWeek).toString();
                else {
                    Arrays.sort(DayOfWeek.getKeys().toArray());
                    throw new StringReadException(s, dayOfWeek, Arrays.toString(DayOfWeek.getKeys().toArray()));
                }

                if (NumberLesson.containsKey(numberLesson))
                    strings[2] = NumberLesson.get(numberLesson).toString();
                else
                    throw new StringReadException(s, numberLesson, Arrays.toString(NumberLesson.getKeys().toArray()));

                tl.add(String.join(CSV_SPLIT, strings));
            }
            lists.add(new ArrayList<>(tl));
            tl.clear();
        }
    }
}
