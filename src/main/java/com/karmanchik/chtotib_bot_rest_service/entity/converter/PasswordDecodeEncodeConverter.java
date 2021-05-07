package com.karmanchik.chtotib_bot_rest_service.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Base64;

@Converter
public class PasswordDecodeEncodeConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        return s.isEmpty() ? null : new String(Base64.getEncoder().encode(s.getBytes()));
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s.isEmpty() ? null : new String(Base64.getDecoder().decode(s));
    }
}
