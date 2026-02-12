package com.aimanager.agent.services;

import com.aimanager.agent.Form.CreateDownStreamNodeForm;
import com.aimanager.agent.Form.UpdateDownStreamNodeForm;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.DownStreamNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.repositories.DownStreamNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DownStreamNodeService {
    private static final Logger logger = LoggerFactory.getLogger(DownStreamNodeService.class);

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    DownStreamNodeRepository downStreamNodeRepository;

    @Autowired
    CommitService commitService;

    /*
     * Retrieves a graph entity by its ID
     * @param graphId The ID of the graph to retrieve
     * @return The retrieved graph entity
     * @throws MissingEntityException if the graph is not found
     */
    public GraphEntity getGraphEntity(Long graphId) throws MissingEntityException {
        Optional<GraphEntity> graphEntity = graphRepository.findById(graphId);
        if (!graphEntity.isPresent()) {
            throw new MissingEntityException("Graph not found");
        }
        return graphEntity.get();
    }

    /**
     * Adds a new DownStream node to a graph
     *
     * @param form The form containing DownStream node details
     * @return The created DownStream node
     * @throws MissingEntityException if the graph is not found
     */
    public DownStreamNode createDownStreamNode(CreateDownStreamNodeForm form) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        DownStreamNode node = new DownStreamNode();
        node.setDownStreamType(form.getDownStreamType());
        node.setGraph(graph);
        node.setStatus(NodeStatus.ACTIVE);
        node = downStreamNodeRepository.save(node);
        commitService.addGraphCommit(graph, node, "Add DownStream node with type " + node.getDownStreamType());
        return node;
    }


    /**
     * Retrieves a DownStream node by its ID and graph
     *
     * @param nodeId
     * @param graph
     * @return
     * @throws MissingEntityException
     */
    public DownStreamNode getDownStreamNodeById(Long nodeId, GraphEntity graph) throws MissingEntityException {
        Optional<DownStreamNode> node = downStreamNodeRepository.findByGraphAndId(graph, nodeId);
        if (node.isPresent()) {
            return node.get();
        } else {
            throw new MissingEntityException("DownStream not found in the specified graph");
        }
    }

    /**
     * Retrieves a DownStream node by its ID and graph ID
     *
     * @param graphId
     * @param questionId
     * @return
     * @throws MissingEntityException
     */
    public DownStreamNode getDownStreamNodeById(Long graphId, Long questionId) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(graphId);
        return getDownStreamNodeById(questionId, graph);
    }

    /**
     * Retrieves a paginated list of DownStream nodes for a graph
     *
     * @param graphId  The ID of the graph
     * @param pageable Pagination information
     * @return A paginated list of DownStream nodes
     * @throws MissingEntityException if the graph is not found
     */
    public Page<DownStreamNode> getDownStreamNodesPaginated(Long graphId, Pageable pageable) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(graphId);
        return downStreamNodeRepository.findByGraph(graph, pageable);
    }

    /**
     * Updates an existing DownStream node
     *
     * @param form The form containing updated DownStream node details
     * @return The updated DownStream node
     * @throws MissingEntityException if the graph or DownStream node is not found
     */
    public DownStreamNode updateDownStreamNode(UpdateDownStreamNodeForm form) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        DownStreamNode node = getDownStreamNodeById(form.getNodeId(), graph);
        String oldType = node.getDownStreamType().getName();
        node.setDownStreamType(form.getDownStreamType());
        node = downStreamNodeRepository.save(node);
        commitService.addGraphCommit(graph, node, "Update DownStream node type from " + oldType + " to " + node.getDownStreamType().getName());
        return node;
    }
}
