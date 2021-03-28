package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.User;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class UserRestController implements EntityRestControllerInterface<User> {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/users/{id}")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull Integer id) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, User.class));
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        List<User> all = userService.findAll();
        return ResponseEntity.ok(all);
    }

    @Override
    public ResponseEntity<?> post(User user) {
        return null;
    }

    @Override
    public <S extends User> ResponseEntity<?> put(@NotNull Integer id, @Valid @NotNull S s) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAll() {
        return null;
    }
}
