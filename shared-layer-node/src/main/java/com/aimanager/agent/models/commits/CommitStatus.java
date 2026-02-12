package com.aimanager.agent.models.commits;

import java.util.Arrays;

public enum CommitStatus {

    CREATED("created"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    IGNORED("ignored");

    private String value;

    CommitStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return value;
    }

    public static CommitStatus fromValue(String value) {
        return Arrays.stream(CommitStatus.values())
            .filter(status -> status.getValue().equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid commit status: " + value));
    }

    public static class Aliases {
        public static final String CREATED = "created";
        public static final String IN_PROGRESS = "in_progress";
        public static final String COMPLETED = "completed";
        public static final String IGNORED = "ignored";
    }

}
