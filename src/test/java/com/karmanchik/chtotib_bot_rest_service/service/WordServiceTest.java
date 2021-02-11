package com.karmanchik.chtotib_bot_rest_service.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class WordServiceTest {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");

    @Test
    public void test_wordFiles1() {
        try (InputStream stream = new FileInputStream(FILE_1)) {
            WordService parser = new WordService(stream);
            var timetable = parser.createTimetable();
            System.out.println(timetable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_wordFiles2() {
        try (InputStream stream = new FileInputStream(FILE_2)) {
            WordService parser = new WordService(stream);
            var timetable = parser.createTimetable();
            System.out.println(timetable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}