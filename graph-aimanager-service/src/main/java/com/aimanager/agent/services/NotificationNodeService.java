package com.aimanager.agent.services;

import com.aimanager.agent.Form.GraphForm;
import com.aimanager.agent.models.*;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.NotificationNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
public class NotificationNodeService<N extends GraphNode> {

    @Autowired
    private NotificationNodeRepository notificationNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private CommitService<N> commitService;

    public NotificationNode createNode(GraphEntity graph){
        NotificationNode node = new NotificationNode();
        node.setStatus(NodeStatus.ACTIVE);
        node.setGraph(graph);
        return notificationNodeRepository.save(node);
    }

    /**
     * Create a notification node
     * @param form
     * @return
     */
    public NotificationNode createNode(GraphForm form){
        GraphEntity graph = getGraphById(form.getGraphId());
        NotificationNode node = createNode(graph);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create notification node: "+node.getId());
        commitService.addToCurrentCommit(commit, (N) node);
        commitService.saveCommit(commit);
        return node;
    }

    /**
     * Get a graph by id
     * @param graphId
     * @return
     */
    public GraphEntity getGraphById(Long graphId){
        return graphRepository.findById(graphId).orElseThrow(
            () -> new EntityNotFoundException("Graph not found"));
    }

    /**
     * Get a notification node by id
     * @param graph
     * @param nodeId
     * @return
     */
    public NotificationNode getNodeById(GraphEntity graph, Long nodeId){
        return notificationNodeRepository.findByGraphAndId(graph, nodeId).
        orElseThrow(() -> new EntityNotFoundException("Notification node not found with id: " + nodeId));
    
    }

    /**
     * Get a notification node by id
     * @param graphId
     * @param nodeId
     * @return
     */
    public NotificationNode getNodeById(Long graphId, Long nodeId){
        GraphEntity graph = getGraphById(graphId);
        return getNodeById(graph, nodeId);
    }

}
