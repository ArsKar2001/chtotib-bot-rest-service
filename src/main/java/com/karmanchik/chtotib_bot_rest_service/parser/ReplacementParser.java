package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.parser.sequence.Sequence;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class ReplacementParser extends BaseParser {

    @Override
    public List<List<String>> parse(File file) throws IOException, InvalidFormatException {
        return Word.getTables(file).stream()
                .map(table -> table.stream()
                        .map(row -> Arrays.toString(row.toArray()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<List<Map<String, Object>>> parseToListMap(File file) throws IOException, InvalidFormatException {
        return Word.getTables(file).stream()
                .map(table -> table.stream()
                        .skip(1)
                        .filter(row -> row.size() >= Sequence.MAX_COLUMN_SIZE)
                        .map(row -> Map.of(
                                "group_name", row.get(0).trim(),
                                "pair", row.get(1).trim(),
                                "discipline", row.get(2).trim(),
                                "auditorium", row.get(3).trim(),
                                "teachers", Arrays.stream(row.get(4).split(Sequence.DEFAULT_SPLIT))
                                        .map(String::trim)
                                        .collect(Collectors.toList()))
                        ).collect(Collectors.toList()))
                .collect(Collectors.toList()).stream()
                .map(table -> {
                    StringBuilder sb = new StringBuilder();
                    return table.stream()
                            .map(row -> {
                                Map<String, Object> tempMap = new HashMap<>(row);
                                String groupName = (String) tempMap.get("group_name");
                                tempMap.remove("group_name");
                                if (!groupName.isBlank()) {
                                    sb.delete(0, sb.length());
                                    sb.append(groupName);
                                }
                                tempMap.put("group_name", sb.toString());
                                return tempMap;
                            }).collect(Collectors.toList());
                }).collect(Collectors.toList());
    }

    public LocalDate getDateFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("\\d{1,2}\\s+\\S+\\s");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            String s = fileName.substring(matcher.start(), matcher.end());
            String dateStr = (s + Year.now()).trim();
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru")));
        }
        return null;
    }
}
