package com.karmanchik.chtotib_bot_rest_service.parser;

import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

@Log4j
class WordParserTest {

    private WordParser wordParser = new WordParser();
    private final File FILE_1 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx");
    private final File FILE_2 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx");
    private final File FILE_3 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");

    public String getTextFromWordFile(File file) {
        try (var stream = new FileInputStream(file)) {
            return wordParser.wordFileAsText(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void textFromReplacement_1() {
        String text = getTextFromWordFile(FILE_1);
        Date date = new Date(FILE_1.lastModified());
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));

        list.removeIf(String::isBlank);
        System.out.println(date);
        list.forEach(s -> System.out.println(s));
    }

    @Test
    public void textFromReplacement_2() {
        String text = getTextFromWordFile(FILE_2);
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));

        list.removeIf(String::isBlank);
        list.forEach(s -> System.out.println(s));
    }

    @Test
    public void textFromReplacement_3() {
        String text = getTextFromWordFile(FILE_3);
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));

        list.removeIf(String::isBlank);
        list.forEach(s -> System.out.println(s));
    }
}