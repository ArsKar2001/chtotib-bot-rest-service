package com.karmanchik.chtotib_bot_rest_service.model;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import lombok.Getter;

@Getter
public class GroupNone extends Group {
    private static final GroupNone INSTANCE = new GroupNone();

    private final Integer id = 100;
    private final String groupName = "NONE";
    private final String lessons = "[]";

    public static GroupNone getInstance() {
        return INSTANCE;
    }
}
