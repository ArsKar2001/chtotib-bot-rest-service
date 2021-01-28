package com.karmanchik.chtotib_bot_rest_service.task;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Log4j
@Scope("prototype")
@Component
public class ImportTask implements Runnable {

    private final JpaGroupRepository groupRepository;
    private JSONArray jsonArray;

    public ImportTask(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }


    @Override
    public void run() {
        for (Object o : jsonArray) {
            try {
                JSONObject object = (JSONObject) o;
                String group_name = object.getString("group_name");
                JSONArray data = object.getJSONArray("timetable");
                JSONObject timetable = new JSONObject();
                timetable.put("data", data);
                Group group = groupRepository.findByGroupName(group_name)
                        .orElseGet(() -> groupRepository.save(new Group(group_name)));
                group.setTimetable(timetable.toString());
                Group upGroup = groupRepository.save(group);
                log.info("Importing to database: " + upGroup.getGroupName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
