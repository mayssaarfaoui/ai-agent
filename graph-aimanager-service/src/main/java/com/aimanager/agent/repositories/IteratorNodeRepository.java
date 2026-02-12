package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aimanager.agent.models.IteratorNode;
import com.aimanager.agent.models.GraphEntity;
@Repository
public interface IteratorNodeRepository extends JpaRepository<IteratorNode, Long> {

    Page<IteratorNode> findByGraph(GraphEntity graph, Pageable pageable);

    Optional<IteratorNode> findByIdAndGraph(Long id, GraphEntity graph);

}
