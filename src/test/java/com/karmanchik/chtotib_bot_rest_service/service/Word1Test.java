package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Word1Test {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_3 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");

    @Test
    public void test_wordFiles1() {
        try (InputStream stream = new FileInputStream(FILE_1)) {
            var timetable = Word.getText(stream);
            final List<String> stringList = new LinkedList<>(Arrays.asList(timetable.split("\n")));
            stringList.removeIf(String::isBlank);
            stringList.forEach(System.out::println);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_wordFiles2() {
//        try (InputStream stream = new FileInputStream(FILE_2)) {
//            WordService1 parser = new WordService1(stream);
//            var lessons = parser.wordFileAsText();
//            final List<String> stringList = new LinkedList<>(Arrays.asList(lessons.split("\n")));
//            stringList.removeIf(String::isBlank);
//            stringList.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test_wordFiles3() {
//        try (InputStream stream = new FileInputStream(FILE_3)) {
//            WordService1 parser = new WordService1(stream);
//            var lessons = parser.wordFileAsText();
//            final List<String> stringList = new LinkedList<>(Arrays.asList(lessons.split("\n")));
//            stringList.removeIf(String::isBlank);
//            stringList.forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}