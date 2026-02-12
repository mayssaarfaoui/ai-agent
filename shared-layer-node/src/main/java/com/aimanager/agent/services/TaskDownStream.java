package com.aimanager.agent.services;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.Task;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class TaskDownStream<T extends Task> extends DownStreamAgent<Fetchable> {

    /**
     * Constructor to initialize the Downstream Node with an Iterator Node.
     *
     */
    public TaskDownStream() {
        super();
    }

    @Override
    public void process(Object data, GraphNode nextNode, NodeContext context) {
        try {
            Task task = mapper.readValue(data.toString(), Task.class);
            logger.info("Processing Task : {}", task.toString());
            NodeContext nc = buildNextNodeContext(task);
            nc.set("item", task);
            nextNode.process(nc);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data can not be converted to task object.");
        }
    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable data) {
        Task task = (Task) data;
        Map<String, String> params = task.convertToParameters();
        NodeContext nc = new NodeContext();
        nc.set("params", params);
        return nc;
    }
}
