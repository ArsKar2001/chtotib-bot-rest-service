package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GroupService {
    Group save(Group group);
    List<Group> save(MultipartFile file) throws StringReadException, IOException;

    List<Group> findAll();

    Group findById(Integer groupId) throws ResourceNotFoundException;

    void delete(Group group);
}
