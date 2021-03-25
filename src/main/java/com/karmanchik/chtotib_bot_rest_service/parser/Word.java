package com.karmanchik.chtotib_bot_rest_service.parser;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class Word {
    private Word() {
    }

    public static String getText(InputStream stream) throws InvalidFormatException {
        try (InputStream inputStream = stream) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(inputStream));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
