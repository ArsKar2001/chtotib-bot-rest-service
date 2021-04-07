package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
import com.karmanchik.chtotib_bot_rest_service.parser.word.Word;
import org.apache.poi.hssf.record.DVALRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ReplacementServiceImplTest {

    private static final File FILE_1 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_3 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");

    @Test
    void test_File1() {
        try (FileInputStream stream = new FileInputStream(FILE_1)) {
            ReplacementParser parser = new ReplacementParser();
            String text = Word.getText(FILE_1);
            System.out.println(parser.textToJSON(text));
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File2() {
        try (FileInputStream stream = new FileInputStream(FILE_2)) {
            ReplacementParser parser = new ReplacementParser();
            String text = Word.getText(FILE_2);
            parser.textToCSV(text)
                    .forEach(System.out::println);
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File3() {
        try (FileInputStream stream = new FileInputStream(FILE_3)) {
            ReplacementParser parser = new ReplacementParser();
            String text = Word.getText(FILE_3);
            parser.textToCSV(text)
                    .forEach(System.out::println);
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}