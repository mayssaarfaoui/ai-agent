package com.aimanager.agent.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Column;
import javax.persistence.OneToOne;

import lombok.Setter;   

@Entity
@Table(schema = "aimanager", name = "graph_edges")
@Getter
@Setter
public class GraphEdge extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "start_node_id")
    private GraphNode startNode;

    @ManyToOne
    @JoinColumn(name = "target_node_id")
    private GraphNode targetNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected NodeStatus status;

    @OneToOne
    @JoinColumn(name = "replaced_by_edge_id")
    protected GraphEdge replacedBy;

    public GraphEdge cloneAndReplaceStartNode(GraphNode newStartNode) {
        GraphEdge clone = new GraphEdge();
        clone.setStartNode(newStartNode);
        clone.setTargetNode(this.targetNode);
        clone.setStatus(NodeStatus.ACTIVE);
        return clone;
    }

    public GraphEdge cloneAndReplaceTargetNode(GraphNode newTargetNode) {
        GraphEdge clone = new GraphEdge();
        clone.setStartNode(this.startNode);
        clone.setTargetNode(newTargetNode);
        clone.setStatus(NodeStatus.ACTIVE);
        return clone;
    }
    
}
