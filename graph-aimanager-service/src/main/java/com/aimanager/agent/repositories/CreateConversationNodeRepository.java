package com.aimanager.agent.repositories;

import com.aimanager.agent.models.CreateConversationNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateConversationNodeRepository extends JpaRepository<CreateConversationNode, Long> {
    // Additional query methods can be defined here if needed
}
