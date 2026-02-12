package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("START")
public class StartNode extends GraphNode {

    public StartNode() {
        super(NodeType.START);
    }

    public StartNode(Long nodeId) {
        super(NodeType.START, nodeId);
    }

    @Override
    public StartNode clone() {
        StartNode clone = new StartNode();
        super.copyData(clone);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Processing Start Node...");
        for (GraphNode node : getConnections()) {
            node.process(context);
        }
    }
}

