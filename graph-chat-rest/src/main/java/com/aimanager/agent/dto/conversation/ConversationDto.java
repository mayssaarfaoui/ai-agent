package com.aimanager.agent.dto.conversation;

import com.aimanager.agent.dto.MessageDto;
import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.PRConversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.models.conversation.UserConversation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ConversationDto {
    private  UUID id;
    private  Long userId;
    private  LocalDateTime createdAt;
    private  LocalDateTime startedAt;
    private  boolean started;
    private  boolean ended;
    private  boolean skipped;
    private List<MessageDto> messages;

    public ConversationDto (Conversation conversation) {
        this.id = conversation.getId();
        this.userId = conversation.getUserId();
        this.createdAt = conversation.getCreatedAt();
        this.startedAt = conversation.getStartedAt();
        this.started = conversation.isStarted();
        this.ended = conversation.isEnded();
        this.skipped = conversation.isSkipped();
        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            List<MessageDto> messages = conversation.getMessages().stream().map(MessageDto::of).collect(Collectors.toList());
            this.messages = messages;
        }
    }

    public static ConversationDto of(Conversation conversation){
        if (conversation instanceof TaskConversation) {
            return TaskConversationDto.of((TaskConversation) conversation);
        }

        if (conversation instanceof UserConversation) {
            return UserConversationDto.of((UserConversation) conversation);
        }

        if(conversation instanceof PRConversation){
            return PRConversationDto.of((PRConversation) conversation);
        }

        throw new IllegalArgumentException(
                "Unknown Conversation subtype: " + conversation.getClass().getName()
        );
    }
}
