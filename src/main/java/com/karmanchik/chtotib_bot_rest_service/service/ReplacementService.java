package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.models.Replacement;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaReplacementRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ReplacementService extends BaseScheduleService {
    private final WordService wordService;
    private final JpaReplacementRepository replacementRepository;
    private final JpaGroupRepository groupRepository;

    public ReplacementService(WordService wordService, JpaReplacementRepository replacementRepository, JpaGroupRepository groupRepository) {
        this.wordService = wordService;
        this.replacementRepository = replacementRepository;
        this.groupRepository = groupRepository;
    }

    @Async
    public CompletableFuture<List<Replacement>> save(MultipartFile file) {
        //...
        return null;
    }

    public JSONArray fileToJson(InputStream stream) {
        String text = wordService.getText(stream);
        log.debug("!!!!!!!!! reading text from file: \"{}\"", text);
        List<List<String>> ll = textToLists(text);
        log.debug("!!!!!!!!! created list: {}", ll);

        for (var list : ll) {
            for (String s : list) {
                String[] split = s.split(";");

            }
        }
        return null;
    }

    @Override
    public List<List<String>> textToLists(String text) {
        String[] sText = splitText(text);
        var list = createList(sText);
        return splitList(list);
    }

    private List<List<String>> splitList(List<String> list) {
        List<String> nll = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();
        LocalDate date = LocalDate.now();
        String regex = "\\d{1,2}\\s+(\u0434\u0435\u043A\u0430\u0431\u0440\u044F|\u044F\u043D\u0432\u0430\u0440\u044F|\u0444\u0435\u0432\u0440\u0430\u043B\u044F|\u043C\u0430\u0440\u0442\u0430|\u0430\u043F\u0440\u0435\u043B\u044F|\u043C\u0430\u044F|\u0438\u044E\u043D\u044F|\u0438\u044E\u043B\u044F|\u0430\u0432\u0433\u0443\u0441\u0442\u0430|\u0441\u0435\u043D\u0442\u044F\u0431\u0440\u044F|\u043E\u043A\u0442\u044F\u0431\u0440\u044F|\u043D\u043E\u044F\u0431\u0440\u044F)+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;

        for (String s : list) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String s1 = s.substring(matcher.start(), matcher.end());
                date = textToDate(s1);
                lls.add(new LinkedList<>(nll));
                nll.clear();
            } else {
                String s1 = s + ";" + date;
                nll.add(s1);
            }
        }
        lls.add(new LinkedList<>(nll));
        lls.removeIf(List::isEmpty);
        return lls;
    }

    private LocalDate textToDate(String s1) {
        String s2 = s1 + " " + Year.now().getValue();
        return LocalDate.parse(s2, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    private String[] splitText(String text) {
        String rText = text.replace('\t', ';');
        return rText.split("\n");
    }

    private List<String> createList(String[] strings) {
        var ll = new LinkedList<>(Arrays.asList(strings));
        ll.removeIf(String::isBlank);
        ll.forEach(s -> s = s.trim());
        return correctingList(ll);
    }

    private List<String> correctingList(List<String> ll) {
        List<String> nll = new LinkedList<>();
        String regex = "(^(([а-я]|[А-Я])+(\\s?+|\\s?+-\\s?+)\\d{1,2}(\\s?+-\\s?+)(\\d|\\d[а-я])|);|\\d{1,2}\\s+(\u0434\u0435\u043A\u0430\u0431\u0440\u044F|\u044F\u043D\u0432\u0430\u0440\u044F|\u0444\u0435\u0432\u0440\u0430\u043B\u044F|\u043C\u0430\u0440\u0442\u0430|\u0430\u043F\u0440\u0435\u043B\u044F|\u043C\u0430\u044F|\u0438\u044E\u043D\u044F|\u0438\u044E\u043B\u044F|\u0430\u0432\u0433\u0443\u0441\u0442\u0430|\u0441\u0435\u043D\u0442\u044F\u0431\u0440\u044F|\u043E\u043A\u0442\u044F\u0431\u0440\u044F|\u043D\u043E\u044F\u0431\u0440\u044F)+)";
        Pattern pt = Pattern.compile(regex);
        Matcher mt;
        String s3 = "";
        for (String s : ll) {
            mt = pt.matcher(s);
            var s2 = s.split(";");
            if (mt.find()) {
                if (s2[0].equals("")) {
                    String s4 = s3 + s;
                    nll.add(s4);
                } else {
                    s3 = getValidGroupName(s2[0]);
                    nll.add(s);
                }
            }
        }
        return nll;
    }
}
