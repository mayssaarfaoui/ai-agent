package com.aimanager.agent.repository;

import com.aimanager.agent.models.conversation.TaskConversation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TaskConversationRepository extends ConversationRepository<TaskConversation> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find conversations by user ID or other criteria
    public boolean existsByUserIdAndTaskIdAndStartedAtGreaterThan(Long userId, UUID taskId, LocalDateTime updatedAt);
}
