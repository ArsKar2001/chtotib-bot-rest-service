package com.karmanchik.chtotib_bot_rest_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ChtotibBotRestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChtotibBotRestServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Irkutsk"));
    }
}
