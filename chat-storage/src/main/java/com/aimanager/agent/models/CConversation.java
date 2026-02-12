package com.aimanager.agent.models;

import com.aimanager.agent.enums.TaskStatus;
import com.aimanager.agent.models.conversation.TaskConversation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table("conversations")
@Getter
@Setter
public class CConversation {

    @PrimaryKeyClass
    @Setter
    @Getter
    public static class ConversationKey {

        @PrimaryKeyColumn(ordinal = 0, name = "user", type = PrimaryKeyType.PARTITIONED)
        private long user;

        @PrimaryKeyColumn(ordinal = 1, name = "timestamp", type = PrimaryKeyType.CLUSTERED)
        private UUID conversation;

        @PrimaryKeyColumn(ordinal = 2, name = "timestamp", type = PrimaryKeyType.CLUSTERED)
        private Instant timestamp;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ConversationKey)) return false;
            ConversationKey that = (ConversationKey) o;
            return getUser() == that.getUser() && Objects.equals(getConversation(), that.getConversation()) && Objects.equals(getTimestamp(), that.getTimestamp());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUser(), getConversation(), getTimestamp());
        }
    }

    @PrimaryKey
    private ConversationKey id;

    @Column("task_id")
    private UUID taskId;

    @Column("task_title")
    private String taskTitle;

    @Column("task_status")
    private TaskStatus status;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("started")
    private boolean started;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CConversation)) return false;
        CConversation that = (CConversation) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTaskId(), that.getTaskId()) && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTaskId(), getCreatedAt());
    }

    public static CConversation copy(TaskConversation c, CConversation cc) {
        ConversationKey id = new ConversationKey();
        id.user = c.getUserId();
        id.conversation = UUID.randomUUID();
        id.timestamp = Instant.now();
        cc.id = id;
        cc.setTaskId(c.getTaskId());
        cc.setTaskTitle(c.getTaskTitle());
        cc.setStatus(c.getStatus());
        cc.setCreatedAt(c.getCreatedAt());
        cc.setStarted(c.isStarted());
        return cc;
    }
}
