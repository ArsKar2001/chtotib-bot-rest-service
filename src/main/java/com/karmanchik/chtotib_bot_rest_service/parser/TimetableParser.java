package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.model.DayOfWeek;
import com.karmanchik.chtotib_bot_rest_service.model.NumberLesson;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidTeacherName;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidText;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.WeekdayFunc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class TimetableParser extends AbstractParser {

    private final List<List<String>> lists = new LinkedList<>();
    private final File file;

    public TimetableParser(File file) {
        this.file = file;
    }

    @Override
    public List<List<List<String>>> parse() throws IOException, InvalidFormatException {
        return wordFileToList().parallelStream()
                .map(table -> {
                    StringBuilder sb = new StringBuilder();
                    return table.stream()
                            .map(row -> {
                                List<String> tempRow = new LinkedList<>(row);
                                tempRow.remove(1);
                                if (row.size() <= MAX_COLUMN_SIZE + 2) {
                                    String dayOfWeek = row.get(1).toLowerCase();
                                    if (!dayOfWeek.equals(DEFAULT_VALUE)) {
                                        sb.delete(0, sb.length());
                                        sb.append(getDay(dayOfWeek).toString());
                                    }
                                }
                                tempRow.add(1, sb.toString());
                                return tempRow;
                            }).collect(Collectors.toList());
                }).map(table -> listUpDownSplitBySplitter(table, UP_DOWN_SPLIT))
                .map(table -> listSplitBySplitter(table, SPLIT_GROUP_ITEM))
                .map(table -> listRowSplitByPairNumber(table, DEFAULT_SPLIT))
                .collect(Collectors.toList());
    }

    private static List<List<String>> listRowSplitByPairNumber(List<List<String>> table, String split) {
        List<List<String>> tempTable = new ArrayList<>();
        table.forEach(row -> {
            List<String> noneRow = new ArrayList<>(row);
            List<String> upRow = new ArrayList<>();
            List<String> downRow = new ArrayList<>();
            if (row.get(2).contains(split)) {
                row.forEach(s -> {
                    if (s.contains(split)) {
                        String[] ss = s.split(split, SPLIT_LIMIT);
                        upRow.add(ss[0].trim());
                        downRow.add(ss[1].trim());
                    } else {
                        upRow.add(s);
                        downRow.add(s);
                    }
                });
                tempTable.add(upRow);
                tempTable.add(downRow);
            } else {
                tempTable.add(noneRow);
            }
        });
        return tempTable;
    }

    private static List<List<String>> listSplitBySplitter(List<List<String>> table, String split) {
        List<List<String>> tempTable = new ArrayList<>();
        table.forEach(row -> {
            List<String> noneRow = new ArrayList<>(row);
            List<String> upRow = new ArrayList<>();
            List<String> downRow = new ArrayList<>();
            if (isStrContainsArray(row, split)) {
                row.forEach(s -> {
                    if (s.contains(split)) {
                        String[] ss = s.split(split, SPLIT_LIMIT);
                        upRow.add(ss[0].trim().isBlank() ? DEFAULT_VALUE : ss[0].trim());
                        downRow.add(ss[1].trim().isBlank() ? DEFAULT_VALUE : ss[1].trim());
                    } else {
                        upRow.add(s);
                        downRow.add(s);
                    }
                });
                tempTable.add(upRow);
                tempTable.add(downRow);
            } else {
                tempTable.add(noneRow);
            }
        });
        return tempTable;
    }

    private static List<List<String>> listUpDownSplitBySplitter(List<List<String>> table, String split) {
        List<List<String>> tempTable = new ArrayList<>();
        table.forEach(row -> {
            List<String> noneRow = new ArrayList<>(row);
            List<String> upRow = new ArrayList<>();
            List<String> downRow = new ArrayList<>();
            if (isStrContainsArray(row, split)) {
                row.forEach(s -> {
                    if (s.contains(split)) {
                        String[] ss = s.split(split, SPLIT_LIMIT);
                        upRow.add(ss[0].trim());
                        downRow.add(ss[1].trim());
                    } else {
                        upRow.add(s);
                        downRow.add(s);
                    }
                });
                upRow.add(WeekType.UP.name());
                downRow.add(WeekType.DOWN.name());
                tempTable.add(upRow);
                tempTable.add(downRow);
            } else {
                noneRow.add(WeekType.NONE.name());
                tempTable.add(noneRow);
            }
        });
        return tempTable;
    }

    private static boolean isStrContainsArray(List<String> row, String split) {
        return row.stream().anyMatch(s -> s.contains(split));
    }

    private static Integer getDay(String dayOfWeek) {
        for (java.time.DayOfWeek day : java.time.DayOfWeek.values()) {
            String dayRuStr = day.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")).toLowerCase();
            if (dayRuStr.equals(dayOfWeek)) {
                return day.getValue();
            }
        }
        return 0;
    }

    private List<List<List<String>>> wordFileToList() throws InvalidFormatException, IOException {
        return Word.getTablesItems(file).stream()
                .map(table -> table.stream()
                        .map(row -> row.stream()
                                .map(String::trim)
                                .map(s -> s.isBlank() ? DEFAULT_VALUE : s)
                                .map(s -> ValidText.getPatternReplaceSymbol().matcher(s).replaceAll(SPLIT_GROUP_ITEM))
                                .map(s -> {
                                    Matcher mt = ValidGroupName.getMatcher(s);
                                    if (mt.find()) {
                                        return ValidGroupName.getValidGroupName(s.substring(mt.start(), mt.end()));
                                    }
                                    return s;
                                }).collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .peek(this::splitTable)
                .collect(Collectors.toList());
    }

    private void splitTable(List<List<String>> table) {
        table.remove(1);
        List<List<String>> leftList = new ArrayList<>();
        List<List<String>> rightList = new ArrayList<>();
        table.forEach(row -> {
            int size = row.size();
            leftList.add(row.subList(0, size / 2));
            rightList.add(row.subList(size / 2, size));
        });
        table.clear();
        table.addAll(addLists(leftList));
        table.addAll(addLists(rightList));
    }

    private Collection<? extends List<String>> addLists(List<List<String>> list) {
        List<String> firstRow = list.get(0);
        String name = firstRow.get(0);
        list.remove(0);
        return list.stream()
                .map(row -> {
                    List<String> tempRow = new ArrayList<>(row);
                    tempRow.add(0, name);
                    return tempRow;
                }).collect(Collectors.toList());
    }

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<String> textToCSV(String text) throws StringReadException {
        return splitList(text);
    }

    private List<String> splitList(String text) {
        List<String> leftList = new ArrayList<>();
        List<String> rightList = new ArrayList<>();
        List<String> leftStrList = new ArrayList<>();
        List<String> rightStrList = new ArrayList<>();
        List<String> newList = new ArrayList<>();
        listValidValues(text).stream()
                .map(s -> s.split(CSV_SPLIT, SPLIT_LIMIT))
                .forEach(ss -> {
                    for (int i = 0; i < ss.length; i++) {
                        if (i < ss.length / 2) {
                            leftStrList.add(ss[i]);
                        } else
                            rightStrList.add(ss[i]);
                    }
                    leftList.add(String.join(CSV_SPLIT, leftStrList));
                    rightList.add(String.join(CSV_SPLIT, rightStrList));
                    leftStrList.clear();
                    rightStrList.clear();
                });
        newList.addAll(leftList);
        newList.addAll(rightList);
        return newList;
    }

    private List<String> listValidValues(String text) {
        return textToListCorrection(text).stream()
                .filter(s -> ValidText.getPatternCsvStr().matcher(s).find() ||
                        ValidGroupName.getPatternGroupName().matcher(s).find())
                .collect(Collectors.toList());
    }

    private List<String> textToListCorrection(String text) {
        return Arrays.stream(text.replace(OLD_CHAR, NEW_CHAR)
                .split(LINE_SPLIT, SPLIT_LIMIT)).filter(s -> !s.isBlank())
                .map(String::trim)
                .map(s -> ValidText.getPatternReplaceSymbol().matcher(s).replaceAll(SPLIT_GROUP_ITEM))
                .map(s -> Arrays.stream(s.split(CSV_SPLIT, SPLIT_LIMIT))
                        .map(String::trim)
                        .map(s1 -> s1.isBlank() ? DEFAULT_VALUE : s1)
                        .collect(Collectors.joining(CSV_SPLIT)))
                .collect(Collectors.toList());
    }

    @Override
    public JSONArray textToJSON(String text) throws StringReadException {
        JSONArray array = new JSONArray();
        JSONObject object;

        this.textToList(text);

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

    private void textToList(String text) throws StringReadException {
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
        lists.add(new LinkedList<>(tempList));
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

        Pattern ptLesson = Pattern.compile("^([А-Яа-я]+|-);");
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
