package com.karmanchik.chtotib_bot_rest_service.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordParserTest {

    private final WordParser WORD_PARSER = new WordParser();
    private final File FOLDER = new File("src\\main\\resources\\files");
    private final File FILE_1 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx");
    private final File FILE_2 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx");
    private final File FILE_3 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");

    public String getTextFromWordFile(File file) {
        try (var stream = new FileInputStream(file)) {
            return WORD_PARSER.wordFileAsText(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTextFromReplacement_1() {
        String text = getTextFromWordFile(FILE_1);
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));
        String temp_str = "";

        Date date = getDateFromString(FILE_1.getName());

        list.removeIf(String::isBlank);

        for (String str : list) {
            String[] split = str.split("\t");

            if (split[0].isBlank()) split[0] = temp_str;
            else temp_str = split[0];

            String join = String.join("\t", split);

            System.out.println(join);
        }
    }

    public Date getDateFromString(String fileName) {
        //Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx
        // ...

        return null;
    }

    @Test
    public void testTextFromReplacement_2() {
        String text = getTextFromWordFile(FILE_2);
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));

        list.removeIf(String::isBlank);
        list.forEach(s -> System.out.println(s));
    }

    @Test
    public void testTextFromReplacement_3() {
        String text = getTextFromWordFile(FILE_3);
        String[] strings = text.trim().split("\n");
        List<String> list = new LinkedList<>(Arrays.asList(strings));

        list.removeIf(String::isBlank);
        list.forEach(s -> System.out.println(s));
    }

    @Test
    public void testRegex() {
        Pattern pattern = Pattern.compile("Z_A_M_E_N_A_na_.+\\.docx");
        for (File file : FOLDER.listFiles()) {
            final String fileName = file.getName();
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                
            }
        }
    }
}