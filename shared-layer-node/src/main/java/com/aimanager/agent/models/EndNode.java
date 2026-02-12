package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("END")
public class EndNode extends GraphNode {

    public EndNode() {
        super(NodeType.END);
    }
    
    public EndNode(Long nodeId) {
        super(NodeType.END, nodeId);
    }

    @Override
    public EndNode clone() {
        EndNode clone = new EndNode();
        super.copyData(clone);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        sendToUI("Processing End Node. The flow has completed.");
    }
}
