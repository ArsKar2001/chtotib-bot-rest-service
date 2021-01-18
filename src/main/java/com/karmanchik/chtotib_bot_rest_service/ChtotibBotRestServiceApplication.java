package com.karmanchik.chtotib_bot_rest_service;

import com.karmanchik.chtotib_bot_rest_service.rest.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChtotibBotRestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChtotibBotRestServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService service) {
        return args -> {
            service.deleteAll();
            service.init();
        };
    }
}
