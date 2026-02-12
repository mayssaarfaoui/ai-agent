package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.StatementNode;

@Repository
public interface StatementNodeRepository extends JpaRepository<StatementNode, Long> {
    Page<StatementNode> findByGraph(GraphEntity graph, Pageable pageable);
    Optional<StatementNode> findByGraphAndId(GraphEntity graph, Long id);

}
