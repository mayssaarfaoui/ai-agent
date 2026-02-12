package com.aimanager.agent.models;


import com.aimanager.agent.enums.Sender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table("messages")
@Getter
@Setter
public class CMessage {

    @PrimaryKeyClass
    @Setter
    @Getter
    public static class MessageKey {

        @PrimaryKeyColumn(ordinal = 0, name = "user", type = PrimaryKeyType.PARTITIONED)
        private long user;

        @PrimaryKeyColumn(ordinal = 1, name = "conversation", type = PrimaryKeyType.CLUSTERED)
        private UUID conversation;

        @PrimaryKeyColumn(ordinal = 2, name = "timestamp", type = PrimaryKeyType.CLUSTERED)
        private Instant timestamp;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MessageKey)) return false;
            MessageKey that = (MessageKey) o;
            return getUser() == that.getUser() && Objects.equals(getConversation(), that.getConversation()) && Objects.equals(getTimestamp(), that.getTimestamp());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUser(), getConversation(), getTimestamp());
        }
    }

    @Id
    private MessageKey id;

    @Column("sender")
    private Sender sender;

    @Column("content")
    private String content;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CMessage)) return false;
        CMessage cMessage = (CMessage) o;
        return Objects.equals(getId(), cMessage.getId()) && getSender() == cMessage.getSender() && Objects.equals(getCreatedAt(), cMessage.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSender(), getCreatedAt());
    }

    /*public static CMessage copy(Message m, CMessage cm) {
        MessageKey id = new MessageKey();
        id.user = m.getUserId();
        id.conversation=m.getConversation();
        id.timestamp = Instant.now();
        cm.id = id;
        cm.sender=m.getSender();
        cm.content=m.getContent();
        cm.createdAt=m.getCreatedAt();
        return cm;
    }*/
}
