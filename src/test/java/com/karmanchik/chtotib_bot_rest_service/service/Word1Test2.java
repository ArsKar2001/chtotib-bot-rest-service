package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.TimetableParser;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
class Word1Test2 {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");


    private static final File FILE_3 = new File("C:\\Users\\arsen\\YandexDisk\\Учеба\\Дипом\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_4 = new File("C:\\Users\\arsen\\YandexDisk\\Учеба\\Дипом\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");

    @Test
    void testFile_1() {
        try {
            TimetableParser parser = new TimetableParser();
            parser.parse(FILE_1).forEach(table -> table.forEach(System.out::println));
        } catch (StringReadException | IOException | InvalidFormatException e) {
            log.error("Ошибка в файле: {}; {}", FILE_1.getName(), e);
        }
    }

    @Test
    void testFile_2() {
        try {
            TimetableParser parser = new TimetableParser();
            parser.parse(FILE_2).forEach(lists -> lists
                    .forEach(System.out::println));
        } catch (StringReadException | IOException | InvalidFormatException e) {
            log.error("Ошибка в файле: {}; {}", FILE_1.getName(), e);
        }
    }

    @Test
    void testFile_3() {
        try {
            TimetableParser parser = new TimetableParser();
            parser.parse(FILE_3).forEach(lists -> lists
                    .forEach(System.out::println));
        } catch (StringReadException | IOException | InvalidFormatException e) {
            log.error("Ошибка в файле: {}; {}", FILE_1.getName(), e);
        }
    }

    @Test
    void testFile_4() {
        try {
            TimetableParser parser = new TimetableParser();
            parser.parse(FILE_4).forEach(lists -> lists
                    .forEach(System.out::println));
        } catch (StringReadException | IOException | InvalidFormatException e) {
            log.error("Ошибка в файле: {}; {}", FILE_1.getName(), e);
        }
    }
}