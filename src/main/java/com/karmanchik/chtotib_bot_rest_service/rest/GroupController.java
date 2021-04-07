package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.assembler.GroupModelAssembler;
import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class GroupController extends BaseController<Group, GroupService> {
    private final GroupModelAssembler assembler;
    private final GroupService groupService;

    public GroupController(GroupService groupService, GroupModelAssembler assembler) {
        super(groupService);
        this.groupService = groupService;
        this.assembler = assembler;
    }

    @Override
    public ResponseEntity<?> get(@NotNull Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> post(Group group) {
        return null;
    }

    @Override
    public ResponseEntity<?> put(@NotNull Integer id, Group group) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(@NotNull Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteAll(List<Integer> values) {
        return null;
    }
}
