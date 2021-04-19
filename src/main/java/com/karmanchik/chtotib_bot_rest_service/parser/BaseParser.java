package com.karmanchik.chtotib_bot_rest_service.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class BaseParser {
    public abstract List<List<String>> parse(File file) throws IOException, InvalidFormatException;

    public abstract List<List<Map<String, Object>>> parseToListMap(File file) throws IOException, InvalidFormatException;
}
