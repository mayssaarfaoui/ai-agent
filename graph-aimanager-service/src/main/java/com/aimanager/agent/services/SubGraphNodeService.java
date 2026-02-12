package com.aimanager.agent.services;

import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateSubGraphForm;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.SubGraphNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.SubGraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;


@Service
public class SubGraphNodeService<N extends GraphNode> {

    @Autowired
    private SubGraphNodeRepository subGraphNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private CommitService commitService;

       /**
     * Get a graph by id
     * @param graphId
     * @return
     */
    public GraphEntity getGraphById(Long graphId){
        return graphRepository.findById(graphId).orElseThrow(
            () -> new EntityNotFoundException("Graph not found with id : "+graphId));
    }

    /**
     * Get subgraph by ID
     * @param subGraphId
     * @return
     */
    public GraphEntity getSubGraph(Long subGraphId){
        if(!graphRepository.existsById(subGraphId))
            throw new IllegalArgumentException("Subgraph not found with id : "+subGraphId);

        return getGraphById(subGraphId);
    }


    /**
     * Create a sub graph node
     * @param graph
     * @param commitId
     * @return
     */
    public SubGraphNode createSubGraph(GraphEntity graph, Long subGraphId,Long commitId) {
        GraphEntity subGraph= getSubGraph(subGraphId);
        SubGraphNode subGraphNode = new SubGraphNode();
        subGraphNode.setGraph(graph);
        subGraphNode.setSubGraphId(subGraphId);
        subGraphNode.setSubGraphName(subGraph.getName());
        subGraphNode.setSubGraphDescription(subGraph.getDescription());
        subGraphNode.setCommitId(commitId);
        subGraphNode.setStatus(NodeStatus.ACTIVE);
        return subGraphNodeRepository.save(subGraphNode);
    }

    /**
     * Create a sub graph node
     * @param form
     * @return
     */
    public SubGraphNode createSubGraph(CreateSubGraphForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        SubGraphNode subGraphNode = createSubGraph(graph, form.getSubGraphId(),form.getCommitId());
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create sub graph node: "+subGraphNode.getId());
        commitService.addToCurrentCommit(commit, (N) subGraphNode);
        commitService.saveCommit(commit);
        return subGraphNode;
    }

    /**
     * Get a sub graph node by id
     * @param graph
     * @param subGraphNodeId
     * @return
     */
    public SubGraphNode getSubGraphNodeById(GraphEntity graph, Long subGraphNodeId) {
        return subGraphNodeRepository.findByGraphAndId(graph, subGraphNodeId).orElseThrow(
            () -> new EntityNotFoundException("Sub graph node not found with id: "+subGraphNodeId));
    }

    /**
     * Get a sub graph node by id
     * @param graphId
     * @param subGraphNodeId
     * @return
     */
    public SubGraphNode getSubGraphNodeById(Long graphId, Long subGraphNodeId) {
        GraphEntity graph = getGraphById(graphId);
        return getSubGraphNodeById(graph, subGraphNodeId);
    }

}
