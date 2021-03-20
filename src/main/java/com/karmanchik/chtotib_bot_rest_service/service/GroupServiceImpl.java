package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.parser.GroupParser;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service("groupServiceImpl")
public class GroupServiceImpl implements GroupService {
    private final JpaGroupRepository groupRepository;

    public GroupServiceImpl(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> save(MultipartFile file) throws StringReadException {
        List<Group> groups = new LinkedList<>();
        try (InputStream stream = file.getInputStream()) {
            GroupParser parser = new GroupParser(stream);
            JSONArray array = new JSONArray(parser.parse());
            log.info("Saving a json of groups of size {} records", array.length());
            for (Object o : array) {
                JSONObject jsonObject = (JSONObject) o;
                String groupName = jsonObject.getString("group_name");
                JSONArray lessons = jsonObject.getJSONArray("lessons");

                Group group = this.groupRepository.findByGroupName(groupName)
                        .orElseGet(() -> this.groupRepository.save(new Group(groupName)));
                group.setLessons(lessons.toString());
                groupRepository.save(group);
                groups.add(group);
                log.debug("Import group: {}", groupName);
            }
            return groups;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findById(Integer groupId) throws ResourceNotFoundException {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(groupId, Group.class));
    }

    @Override
    public void delete(Group group) {
        groupRepository.delete(group);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }
}
