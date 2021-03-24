package com.karmanchik.chtotib_bot_rest_service.jpa.converter;

import com.karmanchik.chtotib_bot_rest_service.jpa.enums.BotState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class BotStateConverter implements AttributeConverter<BotState,Integer> {
    @Override
    public Integer convertToDatabaseColumn(BotState botState) {
        return botState == null ? null : botState.getCode();
    }

    @Override
    public BotState convertToEntityAttribute(Integer code) {
        return code == null ? null : Stream.of(BotState.values())
                .filter(botState -> botState.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
