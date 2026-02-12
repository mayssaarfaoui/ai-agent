package com.aimanager.agent.services;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.nodes.NodeContext;

import java.util.Map;

public interface DownStreamProcess {
    public boolean isDone();
    public String getMessage();
    public Map<String,String> getOptions();
    public void process(Object data);
}
