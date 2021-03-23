package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

class Word1Test2 {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");

    @Test
    void testFile_1() {
        try (FileInputStream stream = new FileInputStream(FILE_1)) {
            final String text = Word.getText(stream);
            TimetableParser parser = new TimetableParser();
            parser.textToCSV(text)
                    .forEach(System.out::println);
        } catch (StringReadException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFile_2() {
        try (FileInputStream stream = new FileInputStream(FILE_2)) {
            final String text = Word.getText(stream);
            TimetableParser parser = new TimetableParser();
            parser.textToCSV(text)
                    .forEach(System.out::println);
        } catch (StringReadException | IOException e) {
            e.printStackTrace();
        }
    }
}