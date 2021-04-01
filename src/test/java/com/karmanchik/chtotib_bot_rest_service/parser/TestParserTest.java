package com.karmanchik.chtotib_bot_rest_service.parser;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class TestParserTest {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");


    @Test
    void test_File1() {
        try (FileInputStream stream = new FileInputStream(FILE_1)) {
            final String text = Word.getText(stream);
            TestParser parser = new TestParser();
            parser.textToCSV(text).forEach(System.out::println);
        } catch (StringReadException | IOException | InvalidFormatException e) {
            log.error("Ошибка в файле: {}; {}", FILE_1.getName(), e);
        }
    }

    @Test
    void testSplit() {
        String[] split = "-;II;Иностр. язык;203,217;Павлов А.М., Гурулева Н.Ю.;-;II;История;303;Насибулин С.А.".split(";");
        Arrays.stream(split).forEach(System.out::println);
    }
}