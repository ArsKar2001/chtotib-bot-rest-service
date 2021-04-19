package com.karmanchik.chtotib_bot_rest_service.parser.word;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class Word {
    private Word() {
    }

    private static XWPFDocument readDocument(File file) throws InvalidFormatException, IOException {
        return new XWPFDocument(OPCPackage.open(file));
    }

    public static String getText(File file) throws IOException, InvalidFormatException {
        XWPFDocument document = readDocument(file);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        return extractor.getText();
    }

    public static List<List<List<String>>> getTables(File file) throws InvalidFormatException, IOException {
        readDocument(file).getHeaderList();
        return readDocument(file).getTables().stream()
                .map(table -> table.getRows().stream()
                        .map(row -> row.getTableCells().stream()
                                .map(XWPFTableCell::getText)
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
