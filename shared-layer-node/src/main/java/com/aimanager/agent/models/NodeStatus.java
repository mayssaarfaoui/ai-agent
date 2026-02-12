package com.aimanager.agent.models;

import java.util.Arrays;

public enum NodeStatus {

    ACTIVE(Aliases.ACTIVE),
    REPLACED(Aliases.REPLACED);

    private String value;

    NodeStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return value;
    }

    public static NodeStatus fromValue(String value) {
        return Arrays.stream(NodeStatus.values())
            .filter(status -> status.getValue().equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid node status: " + value));
    }

    public static class Aliases {
        public static final String ACTIVE = "active";
        public static final String REPLACED = "replaced";
    }

}
