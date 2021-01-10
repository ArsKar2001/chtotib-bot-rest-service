package com.karmanchik.chtotib_bot_rest_service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Log4j
@Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<Object, String> {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public Object convertToEntityAttribute(String s) {
        try {
            return OBJECT_MAPPER.readValue(s, Object.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
