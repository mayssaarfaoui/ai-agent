package com.aimanager.agent.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimanager.agent.repositories.GoogleTaxonomyNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.models.GoogleTaxonomyNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.Form.GraphForm;
import com.aimanager.agent.models.commits.Commit;
@Service    
public class GoogleTaxonomyNodeService {

    @Autowired
    private GoogleTaxonomyNodeRepository googleTaxonomyNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private CommitService commitService;

    /**
     * Get a graph by id
     * @param graphId
     * @return
     */
    public GraphEntity getGraphById(Long graphId) {
        return graphRepository.findById(graphId).
        orElseThrow(() -> new EntityNotFoundException("Graph not found with id: " + graphId));
    }

    public GoogleTaxonomyNode createGoogleTaxonomyNode(GraphEntity graph) {
        GoogleTaxonomyNode googleTaxonomyNode = new GoogleTaxonomyNode();
        googleTaxonomyNode.setGraph(graph);
        googleTaxonomyNode.setStatus(NodeStatus.ACTIVE);
        return googleTaxonomyNodeRepository.save(googleTaxonomyNode);
    }

    /**
     * Create a google taxonomy node
     * @param form
     * @return
     */
    public GoogleTaxonomyNode createGoogleTaxonomyNode(GraphForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        GoogleTaxonomyNode googleTaxonomyNode = createGoogleTaxonomyNode(graph);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create google taxonomy node: "+googleTaxonomyNode.getId());
        commitService.addToCurrentCommit(commit, googleTaxonomyNode);
        commitService.saveCommit(commit);
        return googleTaxonomyNode;
    }

    /**
     * Get a google taxonomy node by id
     * @param id
     * @return
     */
    public GoogleTaxonomyNode getGoogleTaxonomyNodeById(Long graphId, Long id) {
        GraphEntity graph = getGraphById(graphId);
        return googleTaxonomyNodeRepository.findByGraphAndId(graph, id).
        orElseThrow(() -> new EntityNotFoundException("GoogleTaxonomyNode not found with id: " + id));
    }
    
    

}
