package com.aimanager.agent.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OptionDto {
    private final String key;
    private final String value;

    public OptionDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public OptionDto(String key) {
        this.key = key;
        this.value = "TEXT";
    }
}