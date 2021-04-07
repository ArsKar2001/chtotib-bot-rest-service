package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaUserRepository;
import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.entity.User;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    private final JpaUserRepository userRepository;

    public UserServiceImpl(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        log.info("Save the user {}", user.getId());
        return userRepository.save(user);
    }

    public List<User> saveAll(List<User> t) {
        log.info("Save the users {}", t.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return userRepository.saveAll(t);
    }

    public void deleteById(Integer id) {
        log.info("Delete the user {}", id);
        userRepository.deleteById(id);
    }

    public void deleteAll(List<User> t) {
        log.info("Delete the users {}...", t.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        userRepository.deleteAll(t);
        log.info("Delete the users... OK");
    }

    public void deleteAll() {
        log.info("Delete the all users...");
        userRepository.deleteAll();
        log.info("Delete the all users... OK");
    }

    public Optional<User> findById(Integer id) {
        log.info("Find the user {}", id);
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        log.info("Find the all users");
        return userRepository.findAll();
    }
}
