package com.aimanager.agent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ConfigProperties {
    private String name;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
}
