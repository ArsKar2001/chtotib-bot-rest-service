package com.karmanchik.chtotib_bot_rest_service.parser.validate;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidGroupName {
    private static final Pattern PT_GROUP_NAME =
            Pattern.compile("([А-Я]|[а-я])+(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\d{1,2}(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\S{1,2}");
    private static final Pattern ITEM_GROUP_NAME = Pattern.compile("((\\d+([а-я]|))|([А-Я]|[а-я])+)");

    private ValidGroupName() {
    }

    public static Pattern getPtGroupName() {
        return PT_GROUP_NAME;
    }

    public static Pattern getItemGroupName() {
        return ITEM_GROUP_NAME;
    }

    public static String getValidGroupName(@NotNull String s) {
        List<String> list = new LinkedList<>();
        String s1 = s.replace('-', ' ');
        Matcher mt = ITEM_GROUP_NAME.matcher(s1);
        while (mt.find()) {
            String s2 = s1.substring(mt.start(), mt.end());
            list.add(s2);
        }
        return String.join("-", list);
    }
}