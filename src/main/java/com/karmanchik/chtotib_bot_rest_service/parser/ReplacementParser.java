package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ReplacementParser extends AbstractParser {

    @Override
    public List<List<String>> parse(InputStream stream) throws IOException, InvalidFormatException {
        return Word.getTables(stream).stream()
                .map(table -> table.stream()
                        .map(row -> Arrays.toString(row.toArray()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
