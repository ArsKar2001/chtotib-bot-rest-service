package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.exeption.StringReadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GroupService {
    Group save(Group group);
    void save(MultipartFile file) throws StringReadException, IOException;
}
