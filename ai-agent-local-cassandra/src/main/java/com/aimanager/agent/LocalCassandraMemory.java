package com.aimanager.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocalCassandraMemory {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LocalCassandraMemory.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
