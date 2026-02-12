package com.aimanager.agent.data;

import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.models.conversation.UserConversation;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MessageDto<C extends Conversation> {
    private final UUID id;
    private final Long userId;
    private final boolean started;
    Map<String,String> data;

    public void setDataForTaskConversation(TaskConversation conversation) {
        this.data = new HashMap<>();
        data.put("taskId", conversation.getTaskId().toString());
        data.put("taskTitle", conversation.getTaskTitle());
        data.put("taskStatus", conversation.getStatus().name());
    }

    public void setDataForUserConversation(UserConversation conversation) {
        this.data = new HashMap<>();
    }

    public void setDataForTaskConversation(C conversation){
        if (conversation instanceof TaskConversation) {
            setDataForTaskConversation((TaskConversation) conversation);
            return;
        }

        if (conversation instanceof UserConversation) {
            setDataForUserConversation((UserConversation) conversation);
            return;
        }

        throw new IllegalArgumentException(
                "Unknown Conversation subtype: " + conversation.getClass().getName()
        );
    }

    public MessageDto(C conversation) {
        this.id = conversation.getId();
        this.userId = conversation.getUserId();
        this.started = conversation.isStarted();
        //set conversation details
        setDataForTaskConversation(conversation);
    }
}
