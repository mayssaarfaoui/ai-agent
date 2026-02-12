package com.aimanager.agent.services;

import com.aimanager.agent.Form.CreateConversationForm;
import com.aimanager.agent.Form.UpdateConversationNodeForm;
import com.aimanager.agent.models.CreateConversationNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.CreateConversationNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CreateConversationNodeService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateConversationNodeService.class);

    @Autowired
    CreateConversationNodeRepository createConversationNodeRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    CommitService commitService;

    public GraphEntity getGraphEntityById(Long id) {
        return graphRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Graph entity not found with id: " + id));
    }

    /**
     * Create a conversation node
     * @param graphEntity
     * @param form
     * @return
     */

    public CreateConversationNode createNode( GraphEntity graphEntity,CreateConversationForm form) {
        CreateConversationNode node = new CreateConversationNode();
        node.setGraph(graphEntity);
        node.setStatus(NodeStatus.ACTIVE);
        node.setConversationType(form.getConversationType());
        node.setConversationGraphId(form.getConversationGraphId());
        node.setConversationCommitId(form.getConversationCommitId());
        return createConversationNodeRepository.save(node);
    }

    /*     * Create a conversation node
     * @param form
     * @return
     */
    public CreateConversationNode createConversationNode(CreateConversationForm form) {
        GraphEntity graph = getGraphEntityById(form.getGraphId());
        CreateConversationNode node = createNode(graph,form);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create fetch node: "+node.getId()+" for conversations.");
        commitService.addToCurrentCommit(commit, node);
        commitService.saveCommit(commit);
        return  node;
    }

    /**
     * Get a conversation node by ID
     * @param id
     * @return
     */
    public CreateConversationNode getConversationNodeById(Long id) {
        return createConversationNodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conversation node not found with id: " + id));
    }

    /**
     * Update a conversation node
     * @param form
     * @return
     */
    public CreateConversationNode editConversationNode(UpdateConversationNodeForm form) {
        CreateConversationNode node = getConversationNodeById(form.getNodeId());
        node.setConversationType(form.getConversationType());
        node.setConversationGraphId(form.getConversationGraphId());
        node.setConversationCommitId(form.getConversationCommitId());
        return createConversationNodeRepository.save(node);
    }

    /**
     * Delete a conversation node
     * @param pageable
     */

    public Page<CreateConversationNode> getAllConversationNodes(Pageable pageable) {
        return createConversationNodeRepository.findAll(pageable);
    }
}
