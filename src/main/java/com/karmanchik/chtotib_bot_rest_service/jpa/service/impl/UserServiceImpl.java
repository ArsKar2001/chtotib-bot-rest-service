package com.karmanchik.chtotib_bot_rest_service.jpa.service.impl;

import com.karmanchik.chtotib_bot_rest_service.jpa.JpaUserRepository;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.User;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JpaUserRepository userRepository;

    @Override
    public <S extends User> S save(S s) {
        return userRepository.save(s);
    }

    @Override
    public <S extends User> List<S> saveAll(List<S> s) {
        return userRepository.saveAll(s);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public <S extends User> void delete(S s) {
        log.info("Delete user {}", s.getId());
        userRepository.delete(s);
    }

    @Override
    public <S extends User> void deleteAll() {
        log.info("Delete all users!");
        userRepository.deleteAllInBatch();
    }


    @Override
    public <S extends User> Optional<S> findById(Integer id) {
        return (Optional<S>) userRepository.findById(id);
    }

    @Override
    public <S extends User> List<S> findAll() {
        return (List<S>) userRepository.findAll();
    }
}
