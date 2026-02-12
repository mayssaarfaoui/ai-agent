package com.aimanager.agent.dto.conversation;

import com.aimanager.agent.models.conversation.PRConversation;
import lombok.Getter;

@Getter
public class PRConversationDto extends ConversationDto{
    private final String prId;
    private final String prTitle;

    public PRConversationDto(PRConversation conversation){
        super(conversation);
        this.prId = conversation.getPrId();
        this.prTitle = conversation.getPrTitle();
    }

    public static PRConversationDto of(PRConversation conversation) {
        return conversation == null ? null : new PRConversationDto(conversation);
    }
}
