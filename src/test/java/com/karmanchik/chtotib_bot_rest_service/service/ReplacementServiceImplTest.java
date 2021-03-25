package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
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
            final ReplacementParser parser = new ReplacementParser(stream);
            System.out.println(parser.parse());
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File2() {
        try (FileInputStream stream = new FileInputStream(FILE_2)) {
            final ReplacementParser parser = new ReplacementParser(stream);
            System.out.println(parser.parse());
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File3() {
        try (FileInputStream stream = new FileInputStream(FILE_3)) {
            final ReplacementParser parser = new ReplacementParser(stream);
            System.out.println(parser.parse());
        } catch (IOException | StringReadException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}