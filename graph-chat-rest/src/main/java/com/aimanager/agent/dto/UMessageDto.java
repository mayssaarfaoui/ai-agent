package com.aimanager.agent.dto;

import com.aimanager.agent.models.UMessage;
import lombok.Getter;

@Getter
public class UMessageDto extends MessageDto{

    private String content;

    public static UMessageDto of(UMessage uMessage){
        return uMessage == null ? null : new UMessageDto(uMessage);
    }

    public UMessageDto(UMessage message){
        super(message);
        this.content = message.getContent();
    }
}
