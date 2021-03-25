package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.validate.ValidGroupName;
import com.karmanchik.chtotib_bot_rest_service.model.Month;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ReplacementParser extends AbstractBaseParser {

    private final InputStream stream;

    public ReplacementParser(InputStream stream) {
        this.stream = stream;
    }

    public String parse() throws StringReadException, InvalidFormatException {
        JSONArray replacements = new JSONArray();
        JSONObject replacement;
        JSONArray lessons;
        JSONObject lesson;
        String groupName = "";

        final String text;
        text = Word.getText(stream);
        final var lists = textToCSV(text);

//        for (var list : lists) {
//            replacement = new JSONObject();
//            lessons = new JSONArray();
//            LocalDate date = LocalDate.now();
//            for (String s : list) {
//                try {
//                    lesson = new JSONObject();
//                    String[] splitStr = s.split(";");
//                    groupName = splitStr[0];
//                    date = LocalDate.parse(splitStr[5]);
//                    lesson.put("number", splitStr[1]);
//                    lesson.put("discipline", splitStr[2]);
//                    lesson.put("auditorium", splitStr[3]);
//                    lesson.put("teacher", splitStr[4]);
//                    lessons.put(lesson);
//                } catch (Exception e) {
//                    throw new StringReadException(s);
//                }
//            }
//            replacement.put("group_name", groupName);
//            replacement.put("lessons", lessons);
//            replacement.put("date", date);
//            replacements.put(replacement);
//        }
        log.debug("Create new JSON: " + replacements.toString());
        return replacements.toString();
    }

    /**
     * Формирует данные в csv из исходного текста
     *
     * @param text Исходный текст из файла Расписания
     * @return данные для импорта в csv-формате
     * @throws StringReadException Неверное чтение строки
     */
    @Override
    public List<List<String>> textToCSV(String text) throws StringReadException {
        String[] sText = splitText(text);
        var list = createList(sText);
        return null;
    }

    private List<List<String>> splitList(List<String> list) throws StringReadException {
        List<String> nll = new LinkedList<>();
        LocalDate date = LocalDate.now();
        Pattern pattern =
                Pattern.compile("\\d{1,2}\\s+(декабря|января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября)+");
        Matcher matcher;

        for (String s : list) {
            try {
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String s1 = s.substring(matcher.start(), matcher.end());
                    date = textToDate(s1);
                } else {
                    String s1 = s + ";" + date;
                    nll.add(s1);
                }
            } catch (Exception e) {
                throw new StringReadException(s, e);
            }
        }
        nll.removeIf(String::isEmpty);
        return listByGroup(nll);
    }

    private LocalDate textToDate(String s1) {
        String s2 = s1 + " " + Year.now().getValue();
        String[] strings = s2.split("\\s");
        strings[1] = Month.isMonth(strings[1]) ?
                String.valueOf(Month.getNumberMonth(strings[1])) : "0";
        String s3 = String.join(" ", strings);
        return LocalDate.parse(s3, DateTimeFormatter.ofPattern("dd M yyyy"));
    }

    private String[] splitText(String text) {
        return text.replace('\t', ';').split("\n");
    }

    private List<String> createList(String[] strings) throws StringReadException {
        var ll = new LinkedList<>(Arrays.asList(strings));
        ll.removeIf(String::isBlank);
        ll.forEach(s -> s = s.trim());
        return correctingList(ll);
    }

    private List<String> correctingList(List<String> ll) throws StringReadException {
        List<String> nll = new LinkedList<>();
        Pattern pt1 = Pattern.compile("^(([а-я]|[А-Я])+(\\s?+|\\s?+-\\s?+)\\d{1,2}(\\s?+-\\s?+)(\\d|\\d[а-я])|);");
        Pattern pt2 = Pattern.compile("\\d{1,2}\\s+(декабря|января|февраля|марта|апреля|мая|июня|июля|августа|сентября|октября|ноября)+");
        Matcher mt1;
        Matcher mt2;
        String s3 = "";
        for (String s : ll) {
            var s2 = s.split(";");
            try {
                mt1 = pt1.matcher(s);
                mt2 = pt2.matcher(s);
                if (mt1.find()) {
                    if (s2[0].equals("")) {
                        String s4 = s3 + s;
                        nll.add(s4);
                    } else {
                        s3 = ValidGroupName.getValidGroupName(s2[0]);
                        String s1 = s3 + s.substring(s.indexOf(';'));
                        nll.add(s1);
                    }
                } else if (mt2.find()) {
                    nll.add(s);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new StringReadException(s, s2.length);
            }
        }
        return nll;
    }

    private List<List<String>> listByGroup(List<String> list) throws StringReadException {
        List<List<String>> lls = new LinkedList<>();
        List<String> nll = new LinkedList<>();
        String group1 = "";
        for (String s : list) {
            try {
                final String s1 = s.split(";")[0];
                if (!group1.equalsIgnoreCase(s1)) {
                    group1 = s1;
                    lls.add(new LinkedList<>(nll));
                    nll.clear();
                }
                nll.add(group1 + s.substring(s.indexOf(';')));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new StringReadException(s, e);
            }
        }
        lls.add(new LinkedList<>(nll));
        lls.removeIf(List::isEmpty);
        return lls;
    }
}
