package com.aimanager.agent.dto;

import com.aimanager.agent.enums.Sender;
import com.aimanager.agent.models.Message;
import com.aimanager.agent.models.SMessage;
import com.aimanager.agent.models.UMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {

    private Sender sender;
    private LocalDateTime createdAt;

    public MessageDto(Message message){
        sender = message.getSender();
        createdAt = message.getCreatedAt();
    }

    public static MessageDto of(Message message){
        switch (message.getSender()){
            case USER:
                return UMessageDto.of((UMessage) message);
            case SYSTEM:
                return SMessageDto.of((SMessage) message);
            default:
                throw new IllegalArgumentException("Unknown message type:" + message.getSender());
        }
    }
}
