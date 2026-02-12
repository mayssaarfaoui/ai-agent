package com.aimanager.agent.repositories;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.SubGraphNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubGraphNodeRepository extends JpaRepository<SubGraphNode, Long> {

    Optional<SubGraphNode> findByGraphAndId(GraphEntity graph, Long id);

    Page<SubGraphNode> findByGraph(GraphEntity graph, Pageable pageable);

    Optional<SubGraphNode> findByIdAndGraph(Long id, GraphEntity graph);

    // Additional methods specific to SubGraphNode can be added here
}
