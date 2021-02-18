package com.karmanchik.chtotib_bot_rest_service.service.word;

import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Log4j
@Component
public class WordServiceImpl implements WordService {

    @Override
    public String getText(InputStream stream) {
        try (InputStream inputStream = stream) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(inputStream));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        } catch (InvalidFormatException | IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
