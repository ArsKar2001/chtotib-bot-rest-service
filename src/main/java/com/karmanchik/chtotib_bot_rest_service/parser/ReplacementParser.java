package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.*;

@Log4j2
public class ReplacementParser extends AbstractParser {

    private final List<String> list = new LinkedList<>();

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<String> textToCSV(String text) throws StringReadException {
//        textToList(text);
        return list;
    }

    /**
     * Формирует данные в json из исходного текста
     *
     * @param text Исходный текст из файла
     * @return массив json
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public JSONArray textToJSON(String text) throws StringReadException {
        JSONArray array = new JSONArray();
        list.clear();
//        this.textToList(text);
//
//        list.forEach(s -> {
//            JSONObject object = new JSONObject();
//            String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
//            String groupName = strings[0];
//            String pairNumber = strings[1];
//            String discipline = strings[2];
//            String auditorium = strings[3];
//            String teacherName = strings[4];
//            LocalDate date = LocalDate.parse(strings[5]);
//
//            object.put("group_name", groupName);
//            object.put("pair_number", pairNumber);
//            object.put("discipline", discipline);
//            object.put("auditorium", auditorium);
//            object.put("teacher_name", teacherName);
//            object.put("date", date);
//
//            array.put(object);
//        });
        return array;
    }

    @Override
    public List<List<String>> parse(InputStream stream) {
        return null;
    }

//    private void textToList(String text) throws StringReadException {
//        list.clear();
//        String cText = textCorrection(text);
//        createListByText(cText);
//        listCorrectionByDate();
//        listCorrectionByGroupName();
//        splitStringByColumnAndSplitToList(SPLIT_GROUP_ITEM, 4, 3, 2);
//        splitStringByColumnAndSplitToList(DEFAULT_SPLIT, 4, 3, 1);
//        listCorrectionAndValidate();
//    }
//
//    private void listCorrectionByDate() {
//        LocalDate date = LocalDate.now();
//        Pattern datePattern = ValidDate.getDatePattern();
//        Pattern groupNamePattern = Pattern.compile("^([А-Яа-я]+(\\s?+-\\s?+|\\s?+)\\d{2}(\\s?+-\\s?+|\\s?+)\\d([а-я]?|)|);");
//        List<String> tempList = new LinkedList<>(list);
//        list.clear();
//
//        for (String s : tempList) {
//            Matcher mt1 = datePattern.matcher(s);
//            Matcher mt2 = groupNamePattern.matcher(s);
//            if (mt1.find()) {
//                String s1 = s.substring(mt1.start(), mt1.end());
//                date = ValidDate.strToDate(s1);
//            } else if (mt2.find()) {
//                String s1 = s + CSV_SPLIT + date;
//                list.add(s1);
//            }
//        }
//    }
//
//    private void listCorrectionByGroupName() {
//        String groupName = "";
//        List<String> tempList = new LinkedList<>(list);
//        list.clear();
//
//
//        for (String s : tempList) {
//            Matcher matcher = ValidGroupName.getMatcher(s);
//            if (matcher.find())
//                groupName = s.substring(matcher.start(), matcher.end());
//            String s2 = groupName + s.substring(s.indexOf(CSV_SPLIT));
//            list.add(s2);
//        }
//    }
//
//    private void createListByText(String cText) {
//        String split = "\n";
//        String[] arrStr = cText.split(split);
//        list.addAll(Arrays.asList(arrStr));
//        list.removeIf(String::isBlank);
//    }
//
//    private String textCorrection(String text) {
//        char oldChar = '\t';
//        char newChar = ';';
//        String rText = text.replace(oldChar, newChar);
//
//        Pattern pattern = Pattern.compile("(\\|{2})");
//        Matcher matcher = pattern.matcher(rText);
//        return matcher.replaceAll(SPLIT_GROUP_ITEM);
//    }
//
//    private void splitStringByColumnAndSplitToList(String split, Integer... column) throws StringReadException {
//        List<Integer> columns = Arrays.asList(column);
//        List<String> tl = new LinkedList<>(list);
//        list.clear();
//
//        List<String> rtl = new LinkedList<>();
//        List<String> ltl = new LinkedList<>();
//
//        List<String> sList = new ArrayList<>();
//
//        StringBuilder rsb;
//        StringBuilder lsb;
//
//        for (String s : tl) {
//            rtl.clear();
//            ltl.clear();
//            sList.clear();
//            rsb = new StringBuilder();
//            lsb = new StringBuilder();
//            String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
//            if (s.contains(split) && !strings[2].contains(split)) {
//                for (int i = 0; i < strings.length; i++) {
//                    try {
//                        if (columns.contains(i) && strings[i].contains(split)) {
//                            final String[] upDown = strings[i].split(split, SPLIT_LIMIT);
//                            rtl.add(upDown[0].trim().isBlank() ? DEFAULT_VALUE : upDown[0].trim());
//                            ltl.add(upDown[1].trim().isBlank() ? DEFAULT_VALUE : upDown[1].trim());
//                        } else {
//                            String s1 = strings[i].trim().isBlank() ? DEFAULT_VALUE : strings[i].trim();
//                            rtl.add(s1);
//                            ltl.add(s1);
//                        }
//                    } catch (Exception e) {
//                        throw new StringReadException(s, strings[i], e);
//                    }
//                }
//                rsb.append(
//                        String.join(CSV_SPLIT, rtl)
//                );
//                lsb.append(
//                        String.join(CSV_SPLIT, ltl)
//                );
//                list.add(rsb.toString());
//            } else {
//                Arrays.stream(strings)
//                        .map(str -> str.trim().isBlank() ? DEFAULT_VALUE : str.trim())
//                        .forEach(sList::add);
//                lsb.append(
//                        String.join(CSV_SPLIT, sList)
//                );
//            }
//            list.add(lsb.toString());
//        }
//    }
//
//    private void listCorrectionAndValidate() throws StringReadException {
//        List<String> tl = new LinkedList<>(list);
//        list.clear();
//
//        for (String s : tl) {
//            String[] strings = s.split(CSV_SPLIT, SPLIT_LIMIT);
//            String groupName = strings[0];
//            String teacher = strings[4];
//
//            if (ValidGroupName.isGroupName(groupName))
//                strings[0] = ValidGroupName.getValidGroupName(groupName);
//            else
//                throw new StringReadException(s, groupName, "АРХ-18-1 или ЗИО-18-2к");
//
//            if (!teacher.equals(DEFAULT_VALUE))
//                if (ValidTeacherName.isTeacher(teacher))
//                    strings[4] = teacher;
//                else
//                    throw new StringReadException(s, teacher, "Иванов И.И.");
//
//            list.add(String.join(CSV_SPLIT, strings));
//        }
//    }
}
