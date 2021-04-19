package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ReplacementServiceImplTest {

    private final File[] files = {
            new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_chetverg_17_dekabrya_nedelya_nizhnyaya_doc.docx"),
            new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_pyatnitsu_18_dekabrya_nedelya_nizhnyaya_doc.docx"),
            new File("src\\main\\resources\\files\\Z_A_M_E_N_A_na_sredu_16_dekabrya_nedelya_nizhnyaya.docx"),
            new File("src\\main\\resources\\files\\З А М Е Н А  на среду 7 апреля неделя нижняя. doc.docx")
    };


    @Test
    void testFiles() {
        ReplacementParser parser = new ReplacementParser();
        for (File file : files) {
            try {
                System.out.println(parser.getDateFromFileName(file.getName()));
                parser.parseToListMap(file).forEach(table ->
                        table.forEach(System.out::println));
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
        }
    }
}