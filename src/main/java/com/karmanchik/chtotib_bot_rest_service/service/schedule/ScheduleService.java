package com.karmanchik.chtotib_bot_rest_service.service.schedule;

import org.json.JSONArray;

public interface ScheduleService {
    JSONArray createScheduleAsJSON(String text);
    JSONArray createReplacementAsJSON(String text);
}
