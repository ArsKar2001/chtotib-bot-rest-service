package com.karmanchik.chtotib_bot_rest_service.service;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseScheduleService {

    abstract List<List<String>> textToLists(@NotNull String text);

    protected String getValidGroupName(@NotNull String s) {
        List<String> list = new LinkedList<>();
        String s1 = s.replace('-', ' ');
        Pattern pt = Pattern.compile("((\\d+([а-я]|))|([А-Я]|[а-я])+)");
        Matcher mt = pt.matcher(s1);

        while (mt.find()) {
            String s2 = s1.substring(mt.start(), mt.end());
            list.add(s2);
        }
        return String.join("-", list);
    }
}
