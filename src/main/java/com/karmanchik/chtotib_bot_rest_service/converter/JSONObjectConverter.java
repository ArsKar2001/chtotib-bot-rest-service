package com.karmanchik.chtotib_bot_rest_service.converter;

import lombok.extern.log4j.Log4j;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Log4j
@Converter(autoApply = true)
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(JSONObject jsObject) {
        String json;
        try {
            json = jsObject.toString();
        } catch (NullPointerException ex) {
            log.warn(ex.getMessage());
            json = "[]";
        }
        return json;
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonDataAsJson) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(jsonDataAsJson);
        } catch (JSONException ex) {
            log.warn(ex.getMessage());
            jsonData = null;
        }
        return jsonData;
    }
}
