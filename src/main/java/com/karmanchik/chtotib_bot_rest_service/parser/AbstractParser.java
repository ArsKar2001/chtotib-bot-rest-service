package com.karmanchik.chtotib_bot_rest_service.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractParser {
    public abstract List<List<String>> parse(InputStream stream) throws IOException, InvalidFormatException;
}
