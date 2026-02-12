package com.aimanager.agent.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.models.commits.CommitStatus;
import com.aimanager.agent.repositories.CommitRepository;
import com.aimanager.agent.repositories.GraphEdgeRepository;
import com.aimanager.agent.repositories.GraphNodeRepository;
@Service    
public class CommitService<N extends GraphNode> {

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private GraphNodeRepository<N> graphNodeRepository;

    @Autowired
    private GraphEdgeRepository graphEdgeRepository;


    public Commit getCommit(Long commitId,GraphEntity graph) throws MissingEntityException {
        return commitRepository.findByIdAndGraph(commitId, graph)
            .orElseThrow(() -> new MissingEntityException("Commit with id: "+commitId+" not found in graph with id: "+graph.getId()));
    }

    public N copyNode(N copiedNode) {
        N node = graphNodeRepository.save((N) copiedNode.clone());
        copiedNode.setStatus(NodeStatus.REPLACED);
        copiedNode.setReplacedBy(node);
        graphNodeRepository.save(copiedNode);
        return node;
    }
    
    public List<GraphNode> getNodeParents(Commit commit, GraphNode node) {
        List<GraphEdge> edges = getEdgesByTargetNode(commit, node);
        return edges.stream().map(edge -> edge.getStartNode()).collect(Collectors.toList());
    }

    public List<GraphNode> getNodeConnections(Commit commit, GraphNode node) {
        List<GraphEdge> edges = getEdgesByStartNode(commit, node);
        return edges.stream().map(edge -> edge.getTargetNode()).collect(Collectors.toList());
    }

    public List<GraphEdge> getEdgesByStartNode(Commit commit,GraphNode startNode) {
        if(commit.getEdges() == null || commit.getEdges().isEmpty()) {
            return new ArrayList<>();
        }
        return commit.getEdges().stream().filter(edge -> edge.getStartNode().equals(startNode)).collect(Collectors.toList());

    }

    public List<GraphEdge> getEdgesByTargetNode(Commit commit, GraphNode targetNode) {
        if(commit.getEdges() == null || commit.getEdges().isEmpty()) {
            return new ArrayList<>();
        }
        return commit.getEdges().stream().filter(edge -> edge.getTargetNode().equals(targetNode)).collect(Collectors.toList());
    }

    public void copyEdgesAndReplaceStartNode(Commit commit, GraphNode startNode, GraphNode newStartNode) {
        List<GraphEdge> edges = getEdgesByStartNode(commit, startNode);
        for (GraphEdge edge : edges) {
            GraphEdge newEdge = edge.cloneAndReplaceStartNode(newStartNode);
            commit.addEdge(graphEdgeRepository.save(newEdge));
            commit.removeEdge(edge);
        }
    }

    public void copyEdgesAndReplaceTargetNode(Commit commit, GraphNode targetNode, GraphNode newTargetNode) {
        List<GraphEdge> edges = getEdgesByTargetNode(commit, targetNode);
        for (GraphEdge edge : edges) {
            GraphEdge newEdge = edge.cloneAndReplaceTargetNode(newTargetNode);
            commit.addEdge(graphEdgeRepository.save(newEdge));
            commit.removeEdge(edge);
        }
    }

    public void copyEdgesAndReplaceNode(Commit commit, GraphNode node, GraphNode newNode) {
        copyEdgesAndReplaceStartNode(commit, node, newNode);
        copyEdgesAndReplaceTargetNode(commit, node, newNode);
    }



    public Commit getFirstGraphCommit(GraphEntity graph) {
        return commitRepository.getFirstGraphCommit(graph);
    }

    public Commit createCommit(GraphEntity graph) {
        Commit commit = new Commit();
        commit.setGraph(graph);
        commit.setStatus(CommitStatus.CREATED);
        commit.setCreatedAt(Calendar.getInstance());
        return commit;
    }

    public Commit initiateCommit(GraphEntity graph) {
        Commit commit = createCommit(graph);
        Commit previousCommit = getLatestGraphCommit(graph);
        if(previousCommit != null) {
            copyCommittedNodes(previousCommit, commit);
        }
        return commit;
    }

    public void closeCommit(Commit commit) {
        commit.setStatus(CommitStatus.COMPLETED);
        commit.setLabel("Completed commit");
        commitRepository.save(commit);
    }

    public Commit getCurrentGraphCommit(GraphEntity graph) {
        return commitRepository.getLatestGraphCommit(graph);
    }

    public Commit getLatestGraphCommit(GraphEntity graph) {
        return commitRepository.getLatestGraphCommit(graph);
    }

    public void copyCommittedNodes(Commit commitToCopy, Commit commit) {
        Set<GraphNode> nodes = commitToCopy.getNodes();
        for(GraphNode node : nodes) {
            commit.addNode(node);
        }

        Set<GraphEdge> edges = commitToCopy.getEdges();
        for(GraphEdge edge : edges) {
            commit.addEdge(edge);
        }
    }

    // Save commit

    public void saveCommit(Commit commit) {

        commitRepository.save(commit);
    }

    // Add one node to graph commit

    public void addGraphCommit(GraphEntity graph, N node, String label) {
        Commit previousCommit = getLatestGraphCommit(graph);
        Commit commit = initiateCommit(graph);
        if(previousCommit != null) {
            copyCommittedNodes(previousCommit, commit);
        }
        commit.setLabel(label);
       
        commit.addNode(node);
        
        saveCommit(commit);
    }

    // Add oneedge to graph commit

    public void addGraphCommit(GraphEntity graph, GraphEdge edge, String label) {
        Commit previousCommit = getLatestGraphCommit(graph);
        Commit commit = initiateCommit(graph);
        if(previousCommit != null) {
            copyCommittedNodes(previousCommit, commit);
        }
        commit.setLabel(label);
        commit.addEdge(edge);
        saveCommit(commit);
    }

    // Add nodes and edges to graph commit
    public void addGraphCommit(GraphEntity graph, List<N> nodes, List<GraphEdge> edges, String label) {
        Commit previousCommit = getLatestGraphCommit(graph);
        Commit commit = initiateCommit(graph);
        if(previousCommit != null) {
            copyCommittedNodes( previousCommit,commit);
        }
        commit.setLabel(label);
        for(N node : nodes) {
            commit.addNode(node);
        }
        for(GraphEdge edge : edges) {
            commit.addEdge(edge);
        }
        saveCommit(commit);
    }

    // Add nodes to current commit

    public void addToCurrentCommit(Commit commit, N node) {
        commit.addNode(node);
    }

    public void addToCurrentCommit(GraphEntity graph, N node) {
        Commit commit = getCurrentGraphCommit(graph);
        addToCurrentCommit(commit, node);
    }

    public void addToCurrentCommit(GraphEntity graph, List<N> nodes) {
        Commit commit = getCurrentGraphCommit(graph);
        for(N node : nodes) {
            addToCurrentCommit(commit, node);
        }
    }

    // Add edges to current commit

    public void addToCurrentCommit(Commit commit, GraphEdge edge) {
        commit.addEdge(edge);
    }   

    public void addToCurrentCommit(GraphEntity graph, GraphEdge edge) {
        Commit commit = getCurrentGraphCommit(graph);
        addToCurrentCommit(commit, edge);
    }

    public void addEdgesToCurrentCommit(GraphEntity graph, List<GraphEdge> edges) {
        Commit commit = getCurrentGraphCommit(graph);
        for(GraphEdge edge : edges) {
            addToCurrentCommit(commit, edge);
        }
    }
    


    // Remove nodes from current commit

    public void removeFromCurrentCommit(Commit commit, N node) {
        if(commit.getNodes().contains(node)) {
            List<GraphEdge> edges = getEdgesByStartNode(commit, node);
            for(GraphEdge edge : edges) {
                commit.removeEdge(edge);
            }
            List<GraphEdge> edges2 = getEdgesByTargetNode(commit, node);
            for(GraphEdge edge : edges2) {
                commit.removeEdge(edge);
            }
            commit.removeNode(node);
        }
    }

    public void removeFromCurrentCommit(GraphEntity graph, N node) {
        Commit commit = getCurrentGraphCommit(graph);
        removeFromCurrentCommit(commit, node);
    }

    public void removeFromCurrentCommit(GraphEntity graph, List<N> nodes) {
        Commit commit = getCurrentGraphCommit(graph);
        for(N node : nodes) {
                removeFromCurrentCommit(commit, node);
            }
    }

    // Remove edges from current commit

    public void removeFromCurrentCommit(Commit commit, GraphEdge edge) {
        if(commit.getEdges().contains(edge)) {
            commit.removeEdge(edge);
        }
    }   

    public void removeFromCurrentCommit(GraphEntity graph, GraphEdge edge) {
        Commit commit = getCurrentGraphCommit(graph);
        removeFromCurrentCommit(commit, edge);
    }

    public void removeEdgesFromCurrentCommit(GraphEntity graph, List<GraphEdge> edges) {
        Commit commit = getCurrentGraphCommit(graph);
        for(GraphEdge edge : edges) {
            removeFromCurrentCommit(commit, edge);
        }
    }
    
}
