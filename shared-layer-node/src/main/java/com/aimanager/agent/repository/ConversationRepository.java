package com.aimanager.agent.repository;

import com.aimanager.agent.models.conversation.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository <C extends Conversation> extends JpaRepository<C, UUID> {

    public boolean existsByUserIdAndStartedAndEnded(Long userId,boolean started,boolean ended);

    public List<C> findByUserId(long userId);

    public Page<C> findByUserId(long userId, Pageable pageable);

   /* @Query("SELECT c " +
            "FROM Conversation c " +
            "WHERE c.userId = :userId " +
            "AND (c.started = false OR (c.started = true AND c.ended = false)) " +
            "ORDER BY c.createdAt ASC "+
            "LIMIT 1")*/

    @Query(value = "SELECT * FROM chatbot.conversations c " +
            "WHERE c.user_id = :userId " +
            "AND c.skipped = false " +
            "AND (c.started = false OR (c.started = true AND c.ended = false)) " +
            "ORDER BY c.created_at ASC " +
            "LIMIT 1", nativeQuery = true)
    Optional<C> findOldestActiveConversationByUserId(@Param("userId") Long userId);

    public Optional<C> findByIdAndUserId(UUID conversationId, Long userId);
}
