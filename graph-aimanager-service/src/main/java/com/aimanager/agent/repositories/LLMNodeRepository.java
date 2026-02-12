package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.LLMNode;

@Repository
public interface LLMNodeRepository extends JpaRepository<LLMNode, Long> {
    Page<LLMNode> findByGraph(GraphEntity graph, Pageable pageable);
    Optional<LLMNode> findByGraphAndId(GraphEntity graph, Long id);

}
