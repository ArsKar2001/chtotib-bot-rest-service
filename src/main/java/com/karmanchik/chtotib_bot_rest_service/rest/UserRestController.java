package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.User;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.service.UserService;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j
@RestController
@RequestMapping("/api/v1/")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Вернет список всех пользователей
     *
     * @return list
     */
    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Вернет пользователя по его userId
     *
     * @param userId user id
     * @return the user by id
     */
    @GetMapping("/user/{id}")
    @ResponseBody
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Integer userId) {
        try {
            User user = userService.findById(userId);
            return ResponseEntity.ok().body(user);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/user_state")
    @ResponseBody
    public ResponseEntity<?> getUserState(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user.getUserState());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/bot_state")
    @ResponseBody
    public ResponseEntity<?> getBotState(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user.getBotState());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/role")
    @ResponseBody
    public ResponseEntity<?> getRole(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user.getRole());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/group")
    @ResponseBody
    public ResponseEntity<?> getGroup(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user.getData());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/group/lessons")
    @ResponseBody
    public ResponseEntity<?> getGroupLessons(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            JSONArray jsonArray = new JSONArray(user.getData());
            return ResponseEntity.ok(jsonArray.toList());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/group/replacement")
    @ResponseBody
    public ResponseEntity<?> getGroupReplacement(@Valid @PathVariable("id") Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user.getData());
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Обновление user
     *
     * @param userId     the user id
     * @param userDetail rhe user detail
     * @return update user
     */
    @PutMapping("/user/{id}")
    @ResponseBody
    public ResponseEntity<?> updateUser(
            @PathVariable(value = "id") Integer userId, @Valid @RequestBody User userDetail) {
        try {
            User user = userService.findById(userId);
            user.setBotStateId(userDetail.getBotStateId());
            user.setUserStateId(userDetail.getUserStateId());
            user.setData(userDetail.getData());
//            user.setTeacher(userDetail.getTeacher());
            user.setRoleId(userDetail.getRoleId());

            final User updateUser = userService.save(user);
            return ResponseEntity.ok(updateUser);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * @param userId идентификатор объекта User
     * @return Id удаленного User
     *
     * Уделение User
     */
    @DeleteMapping("/user/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(
            @PathVariable(value = "id") Integer userId) {
        try {
            User user = userService.findById(userId);
            userService.delete(user);
            return ResponseEntity.ok(userId);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
