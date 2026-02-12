package com.aimanager.agent.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphNode;

@Repository
public interface GraphEdgeRepository extends JpaRepository<GraphEdge, Long> {

    List<GraphEdge> findByStartNode(GraphNode startNode);

    List<GraphEdge> findByTargetNode(GraphNode targetNode);

    void deleteByStartNode(GraphNode startNode);

    void deleteByTargetNode(GraphNode targetNode);

    void deleteByStartNodeAndTargetNode(GraphNode startNode, GraphNode targetNode);

    Optional<GraphEdge> findByStartNodeAndTargetNode(GraphNode startNode, GraphNode targetNode);

    boolean existsByStartNodeAndTargetNode(GraphNode startNode, GraphNode targetNode);

    public int deleteByStartNodeIdInOrTargetNodeIdIn(List<Long> startNodesIds,List<Long> targetNodesIds);

}
