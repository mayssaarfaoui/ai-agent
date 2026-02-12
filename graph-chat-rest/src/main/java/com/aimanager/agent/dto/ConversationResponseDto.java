package com.aimanager.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConversationResponseDto {
    private UUID conversationId;
    VisitorResponseDto response;
}
