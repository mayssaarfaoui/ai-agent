package com.aimanager.agent.services;

import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateLLMForm;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.LLMNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.LLMNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

@Service    
public class LLMNodeService<N extends GraphNode> {

    @Autowired
    private LLMNodeRepository lLMNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private CommitService<N> commitService;    

    /**
     * Get a graph by id
     * @param graphId
     * @return
     */
    public GraphEntity getGraphById(Long graphId) {
        return graphRepository.findById(graphId).
        orElseThrow(() -> new EntityNotFoundException("Graph not found with id: " + graphId));
    }

    /**
     * Create a LLM node
     * @param graph
     * @param prompt
     * @return
     */
    public LLMNode createLLMNode(GraphEntity graph,String prompt) {
        LLMNode node = new LLMNode();
        node.setPrompt(prompt);
        node.setGraph(graph);
        node.setStatus(NodeStatus.ACTIVE);
        return lLMNodeRepository.save(node);
    }

    /**
     * Create a LLM node
     * @param form
     * @return
     */
    public LLMNode createLLMNode(CreateLLMForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        LLMNode node = createLLMNode(graph, form.getPrompt());
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create LLM node: "+node.getId());
        commitService.addToCurrentCommit(commit, (N) node);
        commitService.saveCommit(commit);
        return node;
    }

    /**
     * Get a LLM node by graph and id
     * @param graph
     * @param id
     * @return
     */
    public LLMNode getLLMNodeByGraphAndId(GraphEntity graph, Long id) {
        return lLMNodeRepository.findByGraphAndId(graph, id).
        orElseThrow(() -> new EntityNotFoundException("LLM node not found with id: " + id));
    }

    /**
     * Get a LLM node by id
     * @param graphId
     * @param id
     * @return
     */
    public LLMNode getLLMNodeById(Long graphId, Long id) {
        GraphEntity graph = getGraphById(graphId);
        return getLLMNodeByGraphAndId(graph, id);
    }
}
