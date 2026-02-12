package com.aimanager.agent.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateIteratorNodeForm;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.IteratorNode;
import com.aimanager.agent.models.IteratorType;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.repositories.IteratorNodeRepository;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.models.commits.Commit;
@Service
public class IteratorNodeService<N extends GraphNode> {

    @Autowired
    IteratorNodeRepository iteratorNodeRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    private CommitService<N> commitService;

    public GraphEntity getGraphById(Long id) {  
        return graphRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Graph not found with Id: " + id));
    }

    public IteratorNode createIteratorNode(GraphEntity graph,IteratorType iteratorType) {
        IteratorNode iteratorNode = new IteratorNode();
        iteratorNode.setGraph(graph);
        iteratorNode.setIteratorType(iteratorType);
        iteratorNode.setStatus(NodeStatus.ACTIVE);
        return iteratorNodeRepository.save(iteratorNode);
    }


    public IteratorNode createIteratorNode(CreateIteratorNodeForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        IteratorNode iteratorNode = createIteratorNode(graph, form.getIteratorType());
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create iterator node: "+iteratorNode.getId());
        commitService.addToCurrentCommit(commit, (N) iteratorNode);
        commitService.saveCommit(commit);
        return iteratorNode;
    }

    public IteratorNode getIteratorNodeById(Long id, Long graphId) {
        GraphEntity graph = getGraphById(graphId);
        return iteratorNodeRepository.findByIdAndGraph(id, graph).orElseThrow(
            () -> new EntityNotFoundException("IteratorNode not found with Id: " + id + " and GraphId: " + graphId));
    }

}
