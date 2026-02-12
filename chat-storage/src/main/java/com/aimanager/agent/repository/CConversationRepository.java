package com.aimanager.agent.repository;

import com.aimanager.agent.models.CConversation;
import com.aimanager.agent.models.CFData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CConversationRepository extends CassandraRepository<CConversation,CConversation.ConversationKey> {
    List<CConversation> findById_User(long userID);
    public boolean existsById_UserAndId_Conversation(long userId, UUID conversationId);
}
