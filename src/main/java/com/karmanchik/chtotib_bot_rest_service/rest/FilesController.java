package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Log4j2
@RestController
@RequestMapping("/api/upload")
public class FilesController {
    private final GroupService groupService;

    public FilesController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public @ResponseBody
    String getMesInfo() {
        return "Можете загружать файлы";
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<String> uploadFile(MultipartFile[] files) {
        try {
            log.info("Sending files: {}", Arrays.toString(files));
            for (var file : files) {
                groupService.save(file);
            }
            return ResponseEntity.status(HttpStatus.OK).body("ОК");
        } catch (IOException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
