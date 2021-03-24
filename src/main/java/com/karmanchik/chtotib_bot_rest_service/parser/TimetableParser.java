package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;
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
    public List<List<String>> textToCSV(String text) throws StringReadException {
        textToListOfLists(text);
        splitListsOfLists();
        listsOfListsCorrection();
        splitStringToListOfLists();
        return lists;
    }

    private void textToListOfLists(String text) {
        char newChar = ';';
        char oldChar = '\t';
        String regex = "\n";
        String rText = text.replace(oldChar, newChar).replace('║', ',');
        Pattern pattern = Pattern.compile("(\\|{2}|\u2551)");

        Matcher matcher = pattern.matcher(rText);
        rText = matcher.replaceAll(",");

        String[] array = rText.split(regex);

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
                    continue;
                } else
                    lsb.append(s).append(CSV_SPLIT).append(WeekType.NONE);
                tl.add(lsb.toString());
            }
            lists.add(new LinkedList<>(tl));
        }
    }
}
