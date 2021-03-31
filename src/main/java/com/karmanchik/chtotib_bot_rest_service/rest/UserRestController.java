package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.User;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(userService.findAll());
    }

    @Override
    public ResponseEntity<?> post(User user) {
        return null;
    }

    @Override
    @PutMapping(value = "/users/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> put(@PathVariable("id") @NotNull Integer id,
                                 @RequestBody @Valid User t) {
        try {
            return ResponseEntity.ok()
                    .body(userService.findById(id)
                            .map(user -> {
                                user.setBotState(t.getBotState());
                                user.setUserState(t.getUserState());
                                user.setTeacher(t.getTeacher());
                                user.setGroup(t.getGroup());
                                return user;
                            }).orElseThrow(() -> new ResourceNotFoundException(id, User.class)));
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @Override
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull Integer id) {
        log.info("Deleted user by id {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Override
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteAll() {
        log.info("Deleted all users");
        userService.deleteAll();
        return ResponseEntity.ok("Deleted all users");
    }
}
