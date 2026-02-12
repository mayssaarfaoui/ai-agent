package com.aimanager.agent.nodes;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;

public abstract class WorkflowAgent {
    //The node Id
    private Long id;
    //the node type :
    private NodeType type;

    public WorkflowAgent(NodeType type) {
        this.type = type;
    }

    public Long getNodeKey() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeInfo() {
        return "Node : ID :" + id + " - type : " + type;
    }

    public abstract void execute(NodeContext context);

    public abstract NodeContext buildNextNodeContext(Fetchable f);
}
