package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.model.Lesson;
import com.karmanchik.chtotib_bot_rest_service.repository.JpaGroupRepository;
import lombok.extern.log4j.Log4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Log4j
@RestController
@RequestMapping("/api/")
public class GroupRestController {
    private JpaGroupRepository groupRepository;

    public GroupRestController(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

//    @GetMapping("/groups/{id}")
//    public ResponseEntity<List<Lesson>> getGroup(@PathVariable(name = "id") @Valid Integer id) {
//        List<Lesson> lessons = groupRepository
//                .getListLessonByGroupId(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Не найден Lesson s id "+id));
//        return ResponseEntity.ok().body(lessons);
//    }
}
