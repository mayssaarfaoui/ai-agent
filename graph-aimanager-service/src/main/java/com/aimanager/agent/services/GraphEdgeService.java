package com.aimanager.agent.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphEdgeRepository;

@Service
public class GraphEdgeService {

    @Autowired
    private GraphEdgeRepository graphEdgeRepository;


    /*
     * Check if the two nodes are connected
     * @param startNode the start node
     * @param targetNode the target node
     * @return true if the nodes are connected, false otherwise
     */
    public boolean nodesAreConnected(GraphNode startNode, GraphNode targetNode) {
        return graphEdgeRepository.existsByStartNodeAndTargetNode(startNode, targetNode);
    }

    /*
     * Create an edge between two nodes
     * @param startNode the start node
     * @param targetNode the target node
     * @return the edge
     */
    public GraphEdge createEdge(GraphNode startNode, GraphNode targetNode) {
        GraphEdge edge = new GraphEdge();
        edge.setStartNode(startNode);
        edge.setTargetNode(targetNode);
        edge.setStatus(NodeStatus.ACTIVE);
        return graphEdgeRepository.save(edge);
    }

    /*
     * Get the edge by the start node and the target node
     * @param startNode the start node
     * @param targetNode the target node
     * @return the edge
     */
    public GraphEdge getEdgeByStartNodeAndEndNode(GraphNode startNode, GraphNode targetNode) {
        return graphEdgeRepository.findByStartNodeAndTargetNode(startNode, targetNode)
            .orElseThrow(() -> new IllegalArgumentException(
                "No edge found between start node with id: "+startNode.getId()+" and target node with id: "+targetNode.getId())
                );
    }

    /*
     * Delete an edge
     * @param commit the commit
     * @param edge the edge
     */
    public void deleteEdge(Commit commit, GraphEdge edge) {
        commit.removeEdge(edge);
    }
}
