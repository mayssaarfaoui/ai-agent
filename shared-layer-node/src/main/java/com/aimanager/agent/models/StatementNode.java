package com.aimanager.agent.models;

import java.util.List;

import com.aimanager.agent.nodes.NodeContext;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("STATEMENT")
public class StatementNode extends GraphNode {
    
    private String statement;

    public StatementNode(Long nodeId, String statement) {
        super(NodeType.STATEMENT, nodeId);
        this.statement = statement;
    }

    public StatementNode(String statement) {
        super(NodeType.STATEMENT);
        this.statement = statement;
    }

    public StatementNode() {
        super(NodeType.STATEMENT);
    }

    @Override
    public StatementNode clone() {
        StatementNode clone = new StatementNode();
        super.copyData(clone);
        clone.setStatement(this.statement);
        return clone;
    }

    public String getStatement() {
        return statement;
    }   

    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public void process(NodeContext context) {
        sendToUI("Statement: " + statement);
        /*for (GraphNode node : connections) {
            node.process();
        }*/
        List<GraphNode> connections = getConnections();
        if (connections != null && !connections.isEmpty()) {
            connections.get(0).process(context); // Continue traversal
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+statement;
    }
}
