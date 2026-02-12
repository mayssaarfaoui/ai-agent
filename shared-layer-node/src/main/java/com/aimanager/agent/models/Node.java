package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;

public interface Node {
    public void setup();
    public void process(NodeContext context);
    public void traverse(String nodeId);
    public void copyData(GraphNode clone);
    public GraphNode clone();
}
