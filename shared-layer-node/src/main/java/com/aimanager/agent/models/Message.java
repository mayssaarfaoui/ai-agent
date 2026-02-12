package com.aimanager.agent.models;

import com.aimanager.agent.enums.Sender;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Message {
    private Sender sender;
    private LocalDateTime createdAt;
}
