package com.aimanager.agent.dto;

import com.aimanager.agent.models.GraphEdge;

import lombok.Getter;

@Getter 
public class GraphEdgeDto {

    private Long id;

    private Long startNodeId;

    private Long targetNodeId;

    public GraphEdgeDto(GraphEdge edge) {
        this.id = edge.getId();
        this.startNodeId = edge.getStartNode().getId();
        this.targetNodeId = edge.getTargetNode().getId();
    }

    public static GraphEdgeDto of(GraphEdge edge) {
        return edge == null ? null : new GraphEdgeDto(edge);
    }
    
    
}
