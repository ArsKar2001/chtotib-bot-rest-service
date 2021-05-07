package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.User;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class LoginController {
    private final JpaUserRepository userRepository;

    @GetMapping("/login")
    private ResponseEntity<?> login(@RequestBody @Valid User user) {
        return userRepository.getByLoginAndPassword(user.getLogin(), user.getPassword())
                .map(user1 -> ResponseEntity.ok("OK"))
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким логином или паролем не найден."));
    }
}
