package com.aimanager.agent.repositories;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NotificationNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationNodeRepository extends JpaRepository<NotificationNode, Long> {
    Page<NotificationNode> findByGraph(GraphEntity graph, Pageable pageable);
    Optional<NotificationNode> findByGraphAndId(GraphEntity graph, Long id);

}
