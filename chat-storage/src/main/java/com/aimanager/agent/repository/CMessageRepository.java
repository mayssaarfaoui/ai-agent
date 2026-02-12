package com.aimanager.agent.repository;

import com.aimanager.agent.models.CMessage;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CMessageRepository extends CassandraRepository<CMessage,CMessage.MessageKey> {
    public List<CMessage> findById_UserAndId_Conversation(long userId, UUID conversationId);

    @Query("SELECT * FROM messages WHERE user = ?0 AND conversation = ?1 ORDER BY timestamp DESC LIMIT 1")
    public CMessage findLastMessage(long userId, UUID conversationId);

    @Query("DELETE FROM messages WHERE user = :userId AND conversation = :conversationId")
    void deleteByUserAndConversation(@Param("userId") long userId, @Param("conversationId") UUID conversationId);
}
