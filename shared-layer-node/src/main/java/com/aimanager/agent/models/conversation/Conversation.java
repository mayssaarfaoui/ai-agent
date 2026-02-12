package com.aimanager.agent.models.conversation;

import com.aimanager.agent.models.Message;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table( schema="chatbot", name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;
    protected Long userId;
    protected Long graphId;
    protected Long commitId;
    protected LocalDateTime createdAt;
    protected LocalDateTime startedAt;
    protected boolean started;
    protected boolean ended;
    protected boolean skipped;
    @Transient
    protected List<Message> messages;

    public String getTargetUserName() {
        return "user"+userId;
    }

    public boolean isStarted30MinutesAgo() {
        return startedAt.plusMinutes(5).isBefore(LocalDateTime.now());
    }

}
