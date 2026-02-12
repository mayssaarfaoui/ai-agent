package com.aimanager.agent.repository;

import com.aimanager.agent.models.conversation.PRConversation;
import org.springframework.stereotype.Repository;

@Repository
public interface PRConversationRepository extends ConversationRepository<PRConversation> {
    // Custom query methods can be defined here if needed
}
