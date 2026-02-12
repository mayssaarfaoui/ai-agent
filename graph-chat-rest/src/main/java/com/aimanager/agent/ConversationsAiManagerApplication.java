package com.aimanager.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConversationsAiManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConversationsAiManagerApplication.class, args);
    }
}
