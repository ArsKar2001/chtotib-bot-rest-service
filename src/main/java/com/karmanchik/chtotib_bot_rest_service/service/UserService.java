package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.User;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Integer userId) throws ResourceNotFoundException;

    User save(User user);

    void delete(User user);
}
