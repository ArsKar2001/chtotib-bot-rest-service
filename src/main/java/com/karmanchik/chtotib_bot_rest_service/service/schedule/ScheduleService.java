package com.karmanchik.chtotib_bot_rest_service.service.schedule;

import org.json.JSONArray;

import java.util.Collection;
import java.util.List;

public interface ScheduleService {
    Collection<List<String>> textToLists(String text);
    JSONArray createScheduleAsJSON(String text);
    JSONArray createReplacementAsJSON(String text);
}
