package com.aimanager.agent.services;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.io.File;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aimanager.agent.Form.DeleteNodeForm;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.files.FileStorageService;
import com.aimanager.agent.models.FileAnswer;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;

@Service
public class GraphNodesService <GN extends GraphNode> {


   private static final Logger logger = LoggerFactory.getLogger(GraphNodesService.class);

   @Autowired
   GraphRepository graphRepository;

    @Autowired
    GraphNodeRepository<GN> graphNodeRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    CommitService<GN> commitService;

    /*
     * Get a graph by its id
     * 
     * @param id the id of the graph
     * @return the graph
     * @throws MissingEntityException if the graph is not found
     */
    public GraphEntity getGraph(Long id) throws MissingEntityException {
        logger.info("Getting graph with id: {}", id);
        return graphRepository.findById(id).orElseThrow(
                () -> new MissingEntityException("Graph with id : " + id + " not found"));
    }

    /*
     * Creates a start node for the graph if one doesn't exist already.
     * @return The created start node
     */
    public GN getStartNode(Long graphId) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        if (graphNodeRepository.existsByGraphAndTypeAndStatus(graph, NodeType.START, NodeStatus.ACTIVE)) 
            return graphNodeRepository.findByGraphAndTypeAndStatus(graph, NodeType.START, NodeStatus.ACTIVE).get();
        
        else
            throw new MissingEntityException("Start node of the graph with id : " + graph.getId() + " not found");
        
    }
 

    /*
     * Creates a terminal node for the graph if one doesn't exist already.
     * @return The created terminal node
     */
    public GN getTerminalNode(Long graphId) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        if (graphNodeRepository.existsByGraphAndTypeAndStatus(graph, NodeType.END, NodeStatus.ACTIVE)) 
            return graphNodeRepository.findByGraphAndTypeAndStatus(graph, NodeType.END, NodeStatus.ACTIVE).get();
        else 
            throw new MissingEntityException("Terminal node of the graph with id : " + graph.getId() + " not found");
    }

    /*
     * Gets a node by its id
     * @return The node
     */
    public GN getNodeById(Long nodeId) throws MissingEntityException {
        return graphNodeRepository.findById(nodeId).orElseThrow(() -> new MissingEntityException("Node not found"));
    }

    /*
     * Gets a node by its id and graph id
     * @return The node
     */
    public GN getNodeByIdAndGraph(Long graphId, Long nodeId) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        return graphNodeRepository.findByIdAndGraph(nodeId,graph).orElseThrow(
            () -> new MissingEntityException("Node with id : " + nodeId + " not found in graph with id : " + graphId));
    }

    // ... existing code ...

    /*
     * Gets a page of nodes belonging to a specific graph
     * @param graphId The ID of the graph
     * @param pageable Pagination information
     * @return Page of nodes belonging to the graph
     */
    @Transactional
    public Page<GN> getNodesByGraph(Long graphId, Pageable pageable) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        return graphNodeRepository.findByGraph(graph, pageable);
    }

    /*
     * Removes the edge between the node and its parent nodes
     * @param node The node
     */
    public void removeEdgeWithParentNodes(GN node) {
        List<GN> parentNodes = (List<GN>) node.getParents();
       for (GN parentNode : parentNodes) {
            parentNode.getConnections().remove(node);
            graphNodeRepository.save(parentNode);
        }
    }

    /*
     * Removes the edge between the node and its connected nodes
     * @param node The node
     */
    public void removeEdgeWithConnectedNodes(GN node) {
        List<GN> connectedNodes = (List<GN>) node.getConnections();
        for (GN connectedNode : connectedNodes) {
            connectedNode.getParents().remove(node);
            graphNodeRepository.save(connectedNode);
        }
    }

    /*
     * Deletes the attachments of a node
     * @param node The node
     */
    public void deleteAttachments(GN node) {
        if (node instanceof FileAnswer) {
            FileAnswer fileAnswer = (FileAnswer) node;
            fileStorageService.deleteFile(fileAnswer.getAnswerFilePath());
        }
    }   

    /*
     * Deletes a node by its id
     * @param form The form containing the node id and graph id
     */
    public void deleteNode(DeleteNodeForm form) throws MissingEntityException {
        GN node = getNodeByIdAndGraph(form.getGraphId(), form.getId());
        if (node.getType() == NodeType.START || node.getType() == NodeType.END) {
            throw new MissingEntityException("Start or end node can't be deleted");
        }
        /*removeEdgeWithParentNodes(node);
        removeEdgeWithConnectedNodes(node); 
        deleteAttachments(node);
        graphNodeRepository.delete(node);*/

        Commit commit = commitService.initiateCommit(node.getGraph());
        commit.setLabel("Delete node: "+node.getId());
        commitService.removeFromCurrentCommit(commit, node);
        commitService.saveCommit(commit);
    }

}
