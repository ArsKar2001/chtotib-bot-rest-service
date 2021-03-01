package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exeption.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.models.Group;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaReplacementRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ReplacementService {
    private final WordService wordService;
    private final JpaReplacementRepository replacementRepository;
    private final JpaGroupRepository groupRepository;

    public ReplacementService(WordService wordService, JpaReplacementRepository replacementRepository, JpaGroupRepository groupRepository) {
        this.wordService = wordService;
        this.replacementRepository = replacementRepository;
        this.groupRepository = groupRepository;
    }

    @Async
    public void saveAllFromWordFile(MultipartFile file) throws StringReadException, IOException {
        //...
        final JSONArray json = fileToJson(file.getInputStream());

    }

    public JSONArray fileToJson(InputStream stream) throws StringReadException {
        JSONArray replacements = new JSONArray();
        JSONObject replacement;
        JSONArray lessons;
        JSONObject lesson;

        final String text = this.wordService.getText(stream);
        log.debug("!!!!!!!!! log debug: reading file: text - \"{}\"", text);
        final var lists = textToCSV(text);
        log.debug("!!!!!!!!! log debug: create lists - \"{}\"", Arrays.toString(lists.toArray()));

        for (var list : lists) {
            replacement = new JSONObject();
            lessons = new JSONArray();
            String groupName = "";
            LocalDate date = LocalDate.now();
            for (String s : list) {
                try {
                    lesson = new JSONObject();
                    String[] splitStr = s.split(";");
                    groupName = splitStr[0];
                    date = LocalDate.parse(splitStr[5]);
                    lesson.put("number", splitStr[1]);
                    lesson.put("discipline", splitStr[2]);
                    lesson.put("auditorium", splitStr[3]);
                    lesson.put("teacher", splitStr[4]);
                    lessons.put(lesson);
                } catch (Exception e) {
                    throw new StringReadException(s);
                }
            }
            replacement.put("group_id", );
            replacement.put("lessons", lessons);
            replacement.put("date", date);
            replacements.put(replacement);
        }
        log.debug("Create new JSON: " + replacements.toString());
        return replacements;
    }

    public List<List<String>> textToCSV(String text) throws StringReadException {
        String[] sText = splitText(text);
        var list = createList(sText);
        return splitList(list);
    }

    public String getValidGroupName(@NotNull String s) {
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

    private List<List<String>> splitList(List<String> list) throws StringReadException {
        List<String> nll = new LinkedList<>();
        LocalDate date = LocalDate.now();
        String regex = "\\d{1,2}\\s+(\u0434\u0435\u043A\u0430\u0431\u0440\u044F|\u044F\u043D\u0432\u0430\u0440\u044F|\u0444\u0435\u0432\u0440\u0430\u043B\u044F|\u043C\u0430\u0440\u0442\u0430|\u0430\u043F\u0440\u0435\u043B\u044F|\u043C\u0430\u044F|\u0438\u044E\u043D\u044F|\u0438\u044E\u043B\u044F|\u0430\u0432\u0433\u0443\u0441\u0442\u0430|\u0441\u0435\u043D\u0442\u044F\u0431\u0440\u044F|\u043E\u043A\u0442\u044F\u0431\u0440\u044F|\u043D\u043E\u044F\u0431\u0440\u044F)+";
        Pattern pattern = Pattern.compile(regex);
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
        return LocalDate.parse(s2, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    private String[] splitText(String text) {
        String rText = text.replace('\t', ';');
        return rText.split("\n");
    }

    private List<String> createList(String[] strings) throws StringReadException {
        var ll = new LinkedList<>(Arrays.asList(strings));
        ll.removeIf(String::isBlank);
        ll.forEach(s -> s = s.trim());
        return correctingList(ll);
    }

    private List<String> correctingList(List<String> ll) throws StringReadException {
        List<String> nll = new LinkedList<>();
        String regex = "(^(([а-я]|[А-Я])+(\\s?+|\\s?+-\\s?+)\\d{1,2}(\\s?+-\\s?+)(\\d|\\d[а-я])|);|\\d{1,2}\\s+(\u0434\u0435\u043A\u0430\u0431\u0440\u044F|\u044F\u043D\u0432\u0430\u0440\u044F|\u0444\u0435\u0432\u0440\u0430\u043B\u044F|\u043C\u0430\u0440\u0442\u0430|\u0430\u043F\u0440\u0435\u043B\u044F|\u043C\u0430\u044F|\u0438\u044E\u043D\u044F|\u0438\u044E\u043B\u044F|\u0430\u0432\u0433\u0443\u0441\u0442\u0430|\u0441\u0435\u043D\u0442\u044F\u0431\u0440\u044F|\u043E\u043A\u0442\u044F\u0431\u0440\u044F|\u043D\u043E\u044F\u0431\u0440\u044F)+)";
        Pattern pt = Pattern.compile(regex);
        Matcher mt;
        String s3 = "";
        for (String s : ll) {
            var s2 = s.split(";");
            try {
                mt = pt.matcher(s);
                if (mt.find()) {
                    if (s2[0].equals("")) {
                        String s4 = s3 + s;
                        nll.add(s4);
                    } else {
                        s3 = getValidGroupName(s2[0]);
                        nll.add(s);
                    }
                }
            } catch (Exception e) {
                throw new StringReadException(s, ";", s2.length);
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
                final String group2 = getValidGroupName(s1);
                if (!group1.equalsIgnoreCase(group2)) {
                    group1 = group2;
                    lls.add(new LinkedList<>(nll));
                    nll.clear();
                }
                nll.add(group1 + s.substring(s.indexOf(';')));
            } catch (Exception e) {
                throw new StringReadException(s, e);
            }
        }
        lls.add(new LinkedList<>(nll));
        lls.removeIf(List::isEmpty);
        return lls;
    }
}
