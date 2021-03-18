package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.service.GroupService;
import com.karmanchik.chtotib_bot_rest_service.service.ReplacementService;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/api/")
public class FileRestController {
    private final GroupService groupService;
    private final ReplacementService replacementService;

    public FileRestController(GroupService groupService, ReplacementService replacementService) {
        this.groupService = groupService;
        this.replacementService = replacementService;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<String> getMesInfo() {
        return ResponseEntity.status(HttpStatus.OK).body("Можете загружать файлы");
    }

    @PostMapping("/insert/group")
    public @ResponseBody
    ResponseEntity<Object> uploadGroupFile(MultipartFile[] files) {
        try {
            JSONArray json = new JSONArray();
            for (var file : files) {
                log.info("Sending file: {}", file);
                json.put(groupService.save(file));
            }
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (IOException | StringReadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/insert/replacement")
    public @ResponseBody
    ResponseEntity<Object> uploadReplacementFile(MultipartFile file) {
        try {
            log.info("Sending files: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(replacementService.save(file));
        } catch (IOException | StringReadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
