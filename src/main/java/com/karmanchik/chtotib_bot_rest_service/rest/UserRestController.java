package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.models.User;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaUserRepository;
import com.karmanchik.chtotib_bot_rest_service.exeption.ResourceNotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@RestController
@RequestMapping("/api/")
public class UserRestController {
    private final JpaUserRepository userRepository;

    public UserRestController(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Вернет список всех пользователей
     *
     * @return list
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Вернет пользователя по его userId
     *
     * @param userId user id
     * @return the user by id
     * @throws org.apache.velocity.exception.ResourceNotFoundException не найден пользователь по userId
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Integer userId) throws org.apache.velocity.exception.ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
        return ResponseEntity.ok().body(user);
    }

    /**
     * Создает нового user в таблице users
     *
     * @param user user
     * @return новый user
     */
    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Обновление user
     *
     * @param userId     the user id
     * @param userDetail rhe user detail
     * @return update user
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Integer userId, @Valid @RequestBody User userDetail) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
        user.setBotLastMessageId(userDetail.getBotLastMessageId());
        user.setBotStateId(userDetail.getBotStateId());
        user.setUserStateId(userDetail.getUserStateId());
        user.setChatId(userDetail.getChatId());
        user.setGroupId(userDetail.getGroupId());
        user.setName(userDetail.getName());
        user.setRoleId(userDetail.getRoleId());

        final User updateUser = userRepository.save(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteUser(
            @PathVariable(value = "id") Integer userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException(userId, User.class));
        userRepository.delete(user);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
