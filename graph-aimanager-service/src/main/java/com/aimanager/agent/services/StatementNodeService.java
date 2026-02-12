package com.aimanager.agent.services;

import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateStatementForm;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.StatementNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.StatementNodeRepository;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class StatementNodeService<N extends GraphNode> {

    @Autowired
    private StatementNodeRepository statementNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private CommitService<N> commitService;

    public StatementNode createStatementNode(GraphEntity graph,String statement){ 
        StatementNode statementNode = new StatementNode();
        statementNode.setStatement(statement);
        statementNode.setStatus(NodeStatus.ACTIVE);
        statementNode.setGraph(graph);
        return statementNodeRepository.save(statementNode);
    }

    /**
     * Create a statement node
     * @param form
     * @return
     */
    public StatementNode createStatementNode(CreateStatementForm form){ 
        GraphEntity graph = getGraphById(form.getGraphId());
        StatementNode statementNode = createStatementNode(graph, form.getStatement());
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Create statement node: "+statementNode.getId());
        commitService.addToCurrentCommit(commit, (N) statementNode);
        commitService.saveCommit(commit);
        return statementNode;
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
     * Get a statement node by id
     * @param graph
     * @param statementId
     * @return
     */
    public StatementNode getStatementNodeById(GraphEntity graph, Long statementId){
        return statementNodeRepository.findByGraphAndId(graph, statementId).
        orElseThrow(() -> new EntityNotFoundException("Statement node not found with id: " + statementId));
    
    }

    /**
     * Get a statement node by id
     * @param graphId
     * @param statementId
     * @return
     */
    public StatementNode getStatementNodeById(Long graphId, Long statementId){
        GraphEntity graph = getGraphById(graphId);
        return getStatementNodeById(graph, statementId);
    }

}
