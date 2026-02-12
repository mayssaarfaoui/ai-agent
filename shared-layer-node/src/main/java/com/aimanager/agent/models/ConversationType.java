package com.aimanager.agent.models;

public enum ConversationType {
    PR_CONVERSATION(Aliases.PR_CONVERSATION),
    TASK_CONVERSATION(Aliases.TASK_CONVERSATION);

    private final String type;

    ConversationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public String getName() {
        return getType();
    }

    public static class Aliases {
        public static final String PR_CONVERSATION = "PR_CONVERSATION";
        public static final String TASK_CONVERSATION = "TASK_CONVERSATION";
    }
}


