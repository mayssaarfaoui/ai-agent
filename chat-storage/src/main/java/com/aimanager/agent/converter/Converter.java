package com.aimanager.agent.converter;


import com.aimanager.agent.models.*;
import com.aimanager.agent.models.conversation.TaskConversation;

public class Converter {

    public static TaskConversation of(CConversation cc){
        TaskConversation conversation = new TaskConversation();
        conversation.setId(cc.getId().getConversation());
        conversation.setUserId(cc.getId().getUser());
        conversation.setTaskId(cc.getTaskId());
        conversation.setTaskTitle(cc.getTaskTitle());
        conversation.setStatus(cc.getStatus());
        conversation.setCreatedAt(cc.getCreatedAt());
        conversation.setStarted(cc.isStarted());
        return conversation;
    }
}
