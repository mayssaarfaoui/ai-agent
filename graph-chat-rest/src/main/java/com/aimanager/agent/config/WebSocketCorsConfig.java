package com.aimanager.agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebSocketCorsConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/chat-socket.io/**")
                .allowedOrigins("*") // Allow all origins
                .allowedMethods("GET", "POST", "OPTIONS") // Allowed methods
                .allowCredentials(true); // Allow credentials if needed
    }
}
