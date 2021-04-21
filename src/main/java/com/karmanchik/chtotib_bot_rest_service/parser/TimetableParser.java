package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.entity.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidText;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.karmanchik.chtotib_bot_rest_service.parser.sequence.Sequence.*;

@Log4j2
public class TimetableParser extends BaseParser {
    @Override
    public List<List<String>> parse(File file) throws IOException, InvalidFormatException {
        return wordFileToList(file).parallelStream()
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
                })
                .map(table -> listUpDownSplitBySplitter(table, UP_DOWN_SPLIT))
                .map(table -> listSplitBySplitter(table, SPLIT_GROUP_ITEM))
                .map(table -> listRowSplitByPairNumber(table, DEFAULT_SPLIT))
                .map(table -> table.stream()
                        .filter(this::isNotNonRow)
                        .collect(Collectors.toList()))
                .map(table -> table.stream()
                        .map(row -> String.join(CSV_SPLIT, row))

                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<List<Map<String, Object>>> parseToListMap(File file) throws IOException, InvalidFormatException {
        return null;
    }

    private boolean isNotNonRow(List<String> list) {
        return list.stream()
                .filter(s -> s.contains(DEFAULT_VALUE) || s.isBlank())
                .count() < 3;
    }

    private List<String> toTeacherNamesList(String teachers) {
        return Arrays.stream(teachers.split(DEFAULT_SPLIT))
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

    private List<List<List<String>>> wordFileToList(File file) throws InvalidFormatException, IOException {
        return Word.getTables(file).stream()
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

}
