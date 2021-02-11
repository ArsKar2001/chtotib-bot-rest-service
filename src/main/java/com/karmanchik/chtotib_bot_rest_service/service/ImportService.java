package com.karmanchik.chtotib_bot_rest_service.service;

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
public class ImportService implements Runnable {

    private final JpaGroupRepository groupRepository;
    private JSONArray jsonArray;

    public ImportService(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (!jsonArray.isEmpty()) {
            for (Object o : jsonArray) {
                try {
                    JSONObject object = (JSONObject) o;
                    String group_name = object.getString("group_name");
                    JSONArray timetable = object.getJSONArray("timetable");
                    Group group = groupRepository.findByGroupName(group_name)
                            .orElseGet(() -> groupRepository.save(new Group(group_name)));
                    group.setTimetable(timetable.toString());
                    groupRepository.save(group);
                    log.info("Importing to database: " + group.getGroupName());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("Пришел пустой объект: " + jsonArray.isEmpty());
        }
    }
}
