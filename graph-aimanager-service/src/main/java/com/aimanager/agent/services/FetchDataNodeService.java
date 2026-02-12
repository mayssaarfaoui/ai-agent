package com.aimanager.agent.services;

import javax.persistence.EntityNotFoundException;

import com.aimanager.agent.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateFetchDataNodeForm;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.FetchDataNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;

@Service
public class FetchDataNodeService<GN extends GraphNode> {

    @Autowired
    private FetchDataNodeRepository fetchDataNodeRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    CommitService commitService;

    public GraphEntity getGraphById(Long id) {  
        return graphRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Graph not found with Id: " + id));
    }

    public FetchDataNode createFetchNode(GraphEntity graph,CreateFetchDataNodeForm form, FetchedResponseType responseType, FetchableType fetchableType) {
        FetchDataNode fetchDataNode = new FetchDataNode();
        fetchDataNode.setStatus(NodeStatus.ACTIVE);
        fetchDataNode.setFetchableType(fetchableType);
        fetchDataNode.setFetchServiceUrl(form.getFetchServiceUrl());
        fetchDataNode.setFetchedResponseType(responseType);
        fetchDataNode.setHeaders(form.getHeaders());
        fetchDataNode.setParameters(form.getParameters());
        fetchDataNode.setValidated(false);
        fetchDataNode.setGraph(graph);
        return fetchDataNodeRepository.save(fetchDataNode);
    }

    public FetchDataNode createFetchNode(Long graphId, CreateFetchDataNodeForm form, FetchedResponseType responseType, FetchableType fetchableType) {
        GraphEntity graph = getGraphById(graphId);
        FetchDataNode fetchDataNode = createFetchNode(graph, form, responseType, fetchableType);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create fetch node: "+fetchDataNode.getId()+".");
        commitService.addToCurrentCommit(commit, fetchDataNode);
        commitService.saveCommit(commit);
        return fetchDataNode;
    }

    public FetchDataNode editFetchNode(Long graphId, Long nodeId, CreateFetchDataNodeForm form, FetchedResponseType responseType, FetchableType fetchableType) {
        GraphEntity graph = getGraphById(graphId);
        FetchDataNode fetchDataNode = getFetchNodeByIdAndGraphId(nodeId, graphId);
        fetchDataNode.setFetchableType(fetchableType);
        fetchDataNode.setFetchServiceUrl(form.getFetchServiceUrl());
        fetchDataNode.setFetchedResponseType(responseType);
        fetchDataNode.setHeaders(form.getHeaders());
        fetchDataNode.setParameters(form.getParameters());
        fetchDataNode.setValidated(false);
        fetchDataNode = fetchDataNodeRepository.save(fetchDataNode);
        return fetchDataNode;
    }

    public FetchDataNode getFetchNodeByIdAndGraphId(Long id, Long graphId) {
        GraphEntity graph = getGraphById(graphId);
        return fetchDataNodeRepository.findByIdAndGraph(id, graph).orElseThrow(
            () -> new EntityNotFoundException("FetchDataNode not found with Id: " + id + " and GraphId: " + graphId));
    }

}
