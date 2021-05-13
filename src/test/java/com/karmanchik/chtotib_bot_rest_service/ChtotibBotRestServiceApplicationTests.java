package com.karmanchik.chtotib_bot_rest_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

class ChtotibBotRestServiceApplicationTests {

    @Test
    void contextLoads() {
        byte[] decode = Base64.getDecoder().decode("123rfh456");
        byte[] encode = Base64.getEncoder().encode(decode);

        System.out.println(new String(decode));
        System.out.println(new String(encode));
    }

}
