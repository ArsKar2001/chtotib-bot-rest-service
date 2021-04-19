package com.karmanchik.chtotib_bot_rest_service.parser;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Log4j2
class ReplacementParserTest {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx");
    private static final File FILE_3 = new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx");
    private static final File FILE_4 = new File("C:\\Users\\arsen\\YandexDisk\\Учеба\\Дипом\\З А М Е Н А  на среду 7 апреля неделя нижняя. doc.docx");

//    @Test
//    public void testFile1() {
//        try (InputStream stream = new FileInputStream(FILE_1)) {
//            ReplacementParser parser = new ReplacementParser();
//            parser.parse(stream)
//                    .forEach(ss -> ss.forEach(System.out::println));
//        } catch (IOException | InvalidFormatException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Test
//    public void testFile2() {
//        try (InputStream stream = new FileInputStream(FILE_2)) {
//            ReplacementParser parser = new ReplacementParser();
//            parser.parse(stream)
//                    .forEach(ss -> ss.forEach(System.out::println));
//        } catch (IOException | InvalidFormatException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Test
//    public void testFile3() {
//        try (InputStream stream = new FileInputStream(FILE_3)) {
//            ReplacementParser parser = new ReplacementParser();
//            parser.parse(stream)
//                    .forEach(ss -> ss.forEach(System.out::println));
//        } catch (IOException | InvalidFormatException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Test
//    public void testFile4() {
//        try (InputStream stream = new FileInputStream(FILE_4)) {
//            ReplacementParser parser = new ReplacementParser();
//            parser.parse(stream)
//                    .forEach(ss -> ss.forEach(System.out::println));
//        } catch (IOException | InvalidFormatException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
}