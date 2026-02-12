package com.aimanager.agent.repositories;

import com.aimanager.agent.models.DownStreamNode;
import com.aimanager.agent.models.GraphEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DownStreamNodeRepository extends JpaRepository<DownStreamNode,Long> {
    Page<DownStreamNode> findByGraph(GraphEntity graph, Pageable pageable);
    Optional<DownStreamNode> findByGraphAndId(GraphEntity graph, Long id);
}
