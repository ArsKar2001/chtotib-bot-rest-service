package com.karmanchik.chtotib_bot_rest_service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@Log4j
//@Converter
//public class JsonToMapConverter implements AttributeConverter<Map<String, Object>, String> {
//    @Override
//    @SuppressWarnings("unchecked")
//    public Map<String, Object> convertToDatabaseColumn(Map<String, Object> attribute) {
//        if (attribute == null) {
//            return new HashMap<>();
//        }
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue(attribute, HashMap.class);
//        } catch (IOException e) {
//            log.error("Convert error while trying to convert string(JSON) to map data structure.");
//        }
//        return new HashMap<>();
//    }
//
//    @Override
//    public String convertToEntityAttribute(Map<String, Object> dbData) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.writeValueAsString(dbData);
//        } catch (JsonProcessingException e) {
//            log.error("Could not convert map to json string.");
//            return null;
//        }
//    }
//}
