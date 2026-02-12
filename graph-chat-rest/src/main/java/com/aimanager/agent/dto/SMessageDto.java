package com.aimanager.agent.dto;

import com.aimanager.agent.models.SMessage;
import lombok.Getter;

@Getter
public class SMessageDto extends MessageDto{

    private VisitorResponseDto content;

    public SMessageDto (SMessage message){
        super(message);
        this.content = message.getContent();
    }

    public static SMessageDto of(SMessage sMessage){
        return sMessage == null ? null : new SMessageDto(sMessage);
    }
}
