package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.parser.GroupParser;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Service
public class IGroupService implements GroupService {
    private final JpaGroupRepository groupRepository;

    public IGroupService(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void save(MultipartFile file) throws StringReadException, IOException {
        final GroupParser groupParser = new GroupParser(file.getInputStream());
        JSONArray json = new JSONArray(groupParser.parse());
        log.info("Saving a json of groups of size {} records", json.length());

        for (Object o : json) {
            JSONObject jsonObject = (JSONObject) o;
            String groupName = jsonObject.getString("group_name");
            JSONArray lessons = jsonObject.getJSONArray("lessons");

            Group group = this.groupRepository.findByGroupName(groupName)
                    .orElseGet(() -> this.groupRepository.save(new Group(groupName)));
            group.setLessons(lessons.toString());
            this.groupRepository.save(group);
            log.debug("Import group: {}", groupName);
        }
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }
}
