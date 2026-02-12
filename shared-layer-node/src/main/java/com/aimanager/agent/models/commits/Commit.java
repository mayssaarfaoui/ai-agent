package com.aimanager.agent.models.commits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.aimanager.agent.models.BaseEntity;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphNode;
import org.springframework.format.annotation.DateTimeFormat;

import com.aimanager.agent.models.GraphEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "aimanager", name = "commits")
@Getter
@Setter
public class Commit extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommitStatus status;

    @ManyToOne
    @JoinColumn(name = "graph_id")
    private GraphEntity graph;

    @Column(name = "label")
    private String label;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @Column(nullable = false)
    private Calendar createdAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = "aimanager",
            name = "commit_graph_nodes",
            joinColumns = @JoinColumn(name = "commit_id"),
            inverseJoinColumns = @JoinColumn(name = "graph_node_id"))
    private java.util.Set<GraphNode> nodes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = "aimanager",
            name = "commit_graph_edges",
            joinColumns = @JoinColumn(name = "commit_id"),
            inverseJoinColumns = @JoinColumn(name = "graph_edge_id"))
    private java.util.Set<GraphEdge> edges;

    public void addNode(GraphNode node) {
        if(nodes == null) {
            nodes = getNodes();
        }
        if(nodes == null) {
            setNodes(new java.util.HashSet<>());
        }
        if(!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void addEdge(GraphEdge edge) {
        if(edges == null) {
            edges = getEdges();
        }
        if(edges == null) {
            setEdges(new java.util.HashSet<>());
        }
        if(!edges.contains(edge)) {
            edges.add(edge);
        }
    }

    public void removeNode(GraphNode node) {
        if(nodes == null) {
            nodes = getNodes();
        }
        if(nodes.contains(node)) {
            nodes.remove(node);
        }
        //delete edges connected to the node
        removeEdgesByNode(node);
    }

    public void removeEdge(GraphEdge edge) {
        if(edges == null) {
            edges = getEdges();
        }
        if(edges.contains(edge)) {
            edges.remove(edge);
        }
    }

    public List<GraphNode> getNodeConnections(GraphNode node) {
        List<GraphEdge> edges = getEdgesByStartNode( node);
        return edges.stream().map(edge -> edge.getTargetNode()).collect(Collectors.toList());
    }

    public List<GraphEdge> getEdgesByStartNode(GraphNode startNode) {
        if(getEdges() == null || getEdges().isEmpty()) {
            return new ArrayList<>();
        }
        return edges.stream().filter(edge -> edge.getStartNode().equals(startNode)).collect(Collectors.toList());
    }

    public List<GraphNode> getNodeParents(GraphNode node) {
        List<GraphEdge> edges = getEdgesByTargetNode( node);
        return edges.stream().map(edge -> edge.getStartNode()).collect(Collectors.toList());
    }

    public List<GraphEdge> getEdgesByTargetNode(GraphNode targetNode) {
        if(getEdges() == null || getEdges().isEmpty()) {
            return new ArrayList<>();
        }
        return edges.stream().filter(edge -> edge.getTargetNode().equals(targetNode)).collect(Collectors.toList());
    }

    public void removeEdgesByStartNode(GraphNode startNode) {
        List<GraphEdge> edges = getEdgesByStartNode(startNode);
        for(GraphEdge edge : edges) {
            removeEdge(edge);
        }
    }

    public void removeEdgesByTargetNode(GraphNode targetNode) {
        List<GraphEdge> edges = getEdgesByTargetNode(targetNode);
        for(GraphEdge edge : edges) {
            removeEdge(edge);
        }
    }

    public void removeEdgesByNode(GraphNode node) {
        removeEdgesByStartNode(node);
        removeEdgesByTargetNode(node);
    }

    public boolean hasParent(GraphNode node) {
        return getNodeParents(node).size() > 0;
    }

    public boolean hasChildren(GraphNode node) {
        return getNodeConnections(node).size() > 0;
    }

    public Commit copy() {
        Commit commit = new Commit();
        commit.setStatus(getStatus());
        commit.setGraph(getGraph());
        commit.setLabel(getLabel());
        commit.setCreatedAt(getCreatedAt());
        commit.setNodes(getNodes());
        commit.setEdges(getEdges());
        return commit;
    }
    
}