package com.aimanager.agent.repository;

import com.aimanager.agent.models.conversation.UserConversation;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConversationRepository extends ConversationRepository<UserConversation> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find conversations by user ID or other criteria
}
