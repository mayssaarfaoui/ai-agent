package com.aimanager.agent.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.NodeStatus;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface GraphNodeRepository<GN extends GraphNode> extends JpaRepository<GN, Long> {

    public Optional<GN> findByGraphAndTypeAndStatus(GraphEntity graph, NodeType type, NodeStatus status);
    public boolean existsByGraphAndTypeAndStatus(GraphEntity graph, NodeType type, NodeStatus status);
    public Optional<GN> findByIdAndGraph(Long id, GraphEntity graph);
    public Page<GN> findByParentsContaining(GN parentNode, Pageable pageable);
    public Page<GN> findByGraph(GraphEntity graph, Pageable pageable);
    public List<GN> findAllByGraph(GraphEntity graph);
    public int deleteByGraph(GraphEntity graph);
}
