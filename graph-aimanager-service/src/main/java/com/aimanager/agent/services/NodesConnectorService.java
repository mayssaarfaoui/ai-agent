package com.aimanager.agent.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;

@Service
public class NodesConnectorService<N extends GraphNode> {

    @Autowired
    GraphNodeRepository<N> graphNodeRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    CommitService<N> commitService;

    @Autowired
    GraphEdgeService graphEdgeService;

    /*
     * Checks if the two nodes are of the same graph
     */
    public void checkIfNodesAreOfSameGraph(N node1, N node2) throws MissingEntityException {
        if (!node1.getGraph().getId().equals(node2.getGraph().getId())) {
            throw new MissingEntityException("Nodes are not of the same graph");
        }
    }

     /*
     * Get the node by id
     */

     public N getNodeByIdAndGraph(Long id, GraphEntity graphEntity) throws MissingEntityException {
        return graphNodeRepository.findById(id)
            .orElseThrow(() -> new MissingEntityException("Node not found with id: " + id));
    }

    /*
     * Get the graph by id
     */

    public GraphEntity getGraphById(Long id) throws MissingEntityException {
        return graphRepository.findById(id)
            .orElseThrow(() -> new MissingEntityException("Graph not found with id: " + id));
    }

    /*
     * Checks if the two nodes are already connected
     */

    public void checkIfNodesAlreadyConnected(N startNode, N nextNode) throws MissingEntityException {
        if (graphEdgeService.nodesAreConnected(startNode, nextNode)) {
            throw new MissingEntityException("Nodes are already connected");
        }
    }

    /*
     * Sets the next node for the start node
     * @return The updated start node
     */
    public void setNextNode(Long graphId, Long startNodeId, Long nextNodeId) throws MissingEntityException {
        GraphEntity graphEntity = getGraphById(graphId);
        N startNode = getNodeByIdAndGraph(startNodeId, graphEntity);
        N nextNode = getNodeByIdAndGraph(nextNodeId, graphEntity);
        checkIfNodesAreOfSameGraph(startNode, nextNode);
        checkIfNodesAlreadyConnected(startNode, nextNode);
        Commit commit = commitService.initiateCommit(graphEntity);
        commit.setLabel("Connect nodes: "+startNode.getId()+" and "+nextNode.getId());
        N copiedStartNode = commitService.copyNode(startNode);
        commitService.copyEdgesAndReplaceNode(commit, startNode, copiedStartNode);
        N copiedNextNode = commitService.copyNode(nextNode);
        commitService.copyEdgesAndReplaceNode(commit, nextNode, copiedNextNode);
        GraphEdge edge = graphEdgeService.createEdge(copiedStartNode, copiedNextNode);
        commit.addEdge(edge);
        commitService.removeFromCurrentCommit(commit, startNode);
        commitService.removeFromCurrentCommit(commit, nextNode);
        commitService.addToCurrentCommit(commit, copiedStartNode);
        commitService.addToCurrentCommit(commit, copiedNextNode);
        commitService.saveCommit(commit);
    }

    /*
     * Removes the connection between two nodes
     * @param graphId The ID of the graph containing the nodes
     * @param startNodeId The ID of the node from which to remove the connection
     * @param endNodeId The ID of the node to disconnect from the start node
     * @throws MissingEntityException if any entity is not found or nodes are not in the same graph
     */
    public void removeNodeConnection(Long graphId, Long startNodeId, Long endNodeId) throws MissingEntityException {
        GraphEntity graphEntity = getGraphById(graphId);
        N startNode = getNodeByIdAndGraph(startNodeId, graphEntity);
        N endNode = getNodeByIdAndGraph(endNodeId, graphEntity);
        checkIfNodesAreOfSameGraph(startNode, endNode);
      /*  N copiedStartNode = commitService.copyNode(startNode);
        N copiedEndNode = commitService.copyNode(endNode);
        copiedStartNode.disconnect(copiedEndNode);
        copiedEndNode.removeParent(copiedStartNode);
        graphNodeRepository.save(copiedStartNode);
        graphNodeRepository.save(copiedEndNode);*/
        GraphEdge edge = graphEdgeService.getEdgeByStartNodeAndEndNode(startNode, endNode);
        Commit commit = commitService.initiateCommit(graphEntity);
        commit.setLabel("Disconnect nodes: "+startNode.getId()+" and "+endNode.getId());
        /*commitService.removeFromCurrentCommit(commit, startNode);
        commitService.removeFromCurrentCommit(commit, endNode);
        commitService.addToCurrentCommit(commit, copiedStartNode);
        commitService.addToCurrentCommit(commit, copiedEndNode);*/
        commit.removeEdge(edge);
        commitService.saveCommit(commit);
    }

    /*
     * Gets paginated connections for a specific node
     * @param graphId The ID of the graph containing the node
     * @param nodeId The ID of the node whose connections to retrieve
     * @param page The page number (zero-based)
     * @param size The size of each page
     * @throws MissingEntityException if the node or graph is not found
     */
    public Page<N> getConnectedNodes(Long graphId, Long nodeId, Pageable pageable) throws MissingEntityException {
        GraphEntity graphEntity = getGraphById(graphId);
        N node = getNodeByIdAndGraph(nodeId, graphEntity);
        
        return graphNodeRepository.findByParentsContaining(node, pageable);
    }

}
