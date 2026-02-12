package com.aimanager.agent.dto;

import com.aimanager.agent.models.ConversationType;
import com.aimanager.agent.models.CreateConversationNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversationDto extends NodeDto {

    ConversationType conversationType;
    private Long conversationGraphId;
    private Long conversationCommitId;

    public CreateConversationDto(CreateConversationNode node, boolean full) {
        super(node, full);
        this.conversationType = node.getConversationType();
        this.conversationGraphId = node.getConversationGraphId();
        this.conversationCommitId = node.getConversationCommitId();
    }


    public static CreateConversationDto of(CreateConversationNode node) {
        return node == null ? null : new CreateConversationDto(node, false);
    }

    public static CreateConversationDto off(CreateConversationNode node) {
        return node == null ? null : new CreateConversationDto(node, true);}
}
