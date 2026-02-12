package com.aimanager.agent.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.models.EndNode;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.SubGraphNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.CommitRepository;
import com.aimanager.agent.repositories.GraphRepository;

@Service
public class FlattenGraphService {

    private static final Logger logger = LoggerFactory.getLogger(FlattenGraphService.class);
    
    @Autowired
    CommitRepository commitRepository;

    @Autowired
    GraphRepository graphRepository;


    public GraphEntity getGraph(Long id) {
        return graphRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Graph not found with id: " + id)
        );
    }

    public Commit getCommit(Long commitId) {
        return commitRepository.findById(commitId).orElseThrow(
            () -> new RuntimeException("Commit not found with id: " + commitId)
        );
    }  
    
    public StartNode getStartNode(Commit commit) {
        return (StartNode) commit.getNodes().stream()
            .filter(node -> node.getType().equals(NodeType.START))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Start node not found in commit: " + commit.getId()));
    }

    public EndNode getEndNode(Commit commit) {
        return (EndNode) commit.getNodes().stream()
            .filter(node -> node.getType().equals(NodeType.END))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("End node not found in commit: " + commit.getId()));
    }

    public SubGraphNode hasSubGraphNode(Commit commit) {
        return (SubGraphNode) commit.getNodes().stream()
            .filter(node -> node.getType().equals(NodeType.SUBGRAPH))
            .findFirst()
            .orElse(null);
    }

    public void copyNodesAndEdges(Commit commit, Commit flattenedCommit) {
        GraphEntity flattenedGraph = commit.getGraph();
        for (GraphNode node : commit.getNodes()) {
            node.setGraph(flattenedGraph);
            flattenedCommit.addNode(node);
        }
        for (GraphEdge edge : commit.getEdges()) {
            flattenedCommit.addEdge(edge);
        }
    }

    public void removeNotUsefulEdges(Commit commit, GraphNode node) {
        List<GraphEdge> edgesToRemove = commit.getEdges().stream()
            .filter(edge -> edge.getStartNode().equals(node) || edge.getTargetNode().equals(node))
            .collect(Collectors.toList());
        commit.getEdges().removeAll(edgesToRemove);
    }

    public void removeNotUsefulNode(Commit commit, GraphNode node) {
        removeNotUsefulEdges(commit, node);
        commit.getNodes().remove(node);
    }

    public void replaceSubGraphNode(Commit commit, SubGraphNode subGraphNode, StartNode startNode, EndNode endNode) {

        GraphNode nextNode = commit.getNodeConnections(startNode).get(0);

        GraphNode nextNodeForSubGraphNode = commit.getNodeConnections(subGraphNode).get(0);

        commit.getEdges().forEach(edge -> {
            if (edge.getTargetNode().equals(subGraphNode)) {
                edge.setTargetNode(nextNode);
            }
            if (edge.getTargetNode().equals(endNode)) {
                edge.setTargetNode(nextNodeForSubGraphNode);
            }
          
        });

        removeNotUsefulNode(commit, subGraphNode);
        removeNotUsefulNode(commit, startNode);
        removeNotUsefulNode(commit, endNode);
    }

    /*
     * Flatten a sub graph node
     * 
     * @param commit the commit to flatten
     * @param subGraphNode the sub graph node to flatten
     * @return the flattened commit
     */

    public Commit flattenSubGraphNode(Commit commit, SubGraphNode subGraphNode) {
        Commit flattenedCommit = commit.copy();
        Commit subGraphCommit = getCommit(subGraphNode.getCommitId());
        StartNode startNode = (StartNode) getStartNode(subGraphCommit);
        EndNode endNode = (EndNode) getEndNode(subGraphCommit);
        copyNodesAndEdges(subGraphCommit, flattenedCommit);
        replaceSubGraphNode(flattenedCommit, subGraphNode, startNode, endNode);
        return flattenedCommit;
    }

    /*
     * Flatten the graph
     * 
     * @param commit the commit to flatten
     * @return the flattened commit
     */

    public Commit flattenGraph(Commit commit) {
        logger.info("Flattening graph with id: {}", commit.getId());
        SubGraphNode subGraphNode = hasSubGraphNode(commit);
       while (subGraphNode != null) {
        commit = flattenSubGraphNode(commit, subGraphNode);
        subGraphNode = hasSubGraphNode(commit);
       }
        return commit;
    }

}
