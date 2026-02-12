package com.aimanager.agent.dto.conversation;

import com.aimanager.agent.models.conversation.UserConversation;
import lombok.Getter;

@Getter
public class UserConversationDto extends ConversationDto{

    public UserConversationDto(UserConversation conversation) {
        super(conversation);
    }

    public static UserConversationDto of(UserConversation conversation) {
        return conversation == null ? null:new UserConversationDto(conversation);
    }
}
