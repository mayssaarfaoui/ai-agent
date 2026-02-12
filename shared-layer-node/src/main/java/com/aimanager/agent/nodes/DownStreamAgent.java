package com.aimanager.agent.nodes;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DownStreamAgent<F extends Fetchable> extends WorkflowAgent {

    protected final Logger logger = LoggerFactory.getLogger(DownStreamAgent.class);

    protected Gson gson = new Gson();

    @Autowired
    IteratorAgent iteratorAgent;

    @Autowired
    protected ObjectMapper mapper;

    /**
     * Constructor to initialize the Downstream Node with an Iterator Node.
     *
     */
    public DownStreamAgent() {
        super(NodeType.DownStreamNode);
    }

    protected JsonObject toJsonObject(Object data) {

        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (data instanceof JsonObject) {
            return (JsonObject) data;
        }

        if (data instanceof JsonElement) {
            return ((JsonElement) data).getAsJsonObject();
        }

        if (data instanceof String) {
            return JsonParser.parseString((String) data).getAsJsonObject();
        }

        // Handles LinkedTreeMap, Map, POJO, etc.
        return gson.toJsonTree(data).getAsJsonObject();
    }


    /**
     * Retrieves and processes the next data item from the Iterator Node.
     *
     * @return The processed data item or null if no more items are available.
     */
    public Object getnext() {
        if (iteratorAgent.hasnext()) {
            return iteratorAgent.getnext();
        } else {
            //handleError("No more data available in the Iterator Node.");
            return null;
        }
    }

    /**
     * Processes a data item fetched from the Iterator Node.
     *
     * @param data The data item to process.
     * @return The processed data.
     */
    public abstract void process(Object data,GraphNode nextNode, NodeContext context);


    /**Processes all items in the iterator
     *
     *
     */
    public void processAll(GraphNode nextNode,NodeContext context) {

        //Load data from iterator and process
        Object item = getnext();
        while(item != null){
            process(item,nextNode,context);
            item = getnext();
        }
    }

    /**
     * Handles errors, such as invalid or empty responses from the Iterator Node.
     *
     * @param message The error message.
     */
    private void handleError(String message) {
        throw new IllegalStateException(message);
    }

    /**
     * Delete stored data from cassandra
     * @param context
     */

    public void deleteStoredData(NodeContext context){
        Long nodeId = (Long) context.get("nodeId");
        logger.info("DELETE STORED DATA FROM CASSANDRA WITH NODE ID : {}",nodeId);
        iteratorAgent.deleteStoredData(nodeId);
    }

    @Override
    public void execute(NodeContext context) {
        Long nodeId = (Long) context.get("nodeId");
        logger.info("Iterate through items fetched by node with ID : {}",nodeId);
        iteratorAgent.loadData(nodeId);
        //processAll();
       // deleteStoredData(nodeId);
       /* if(nextNode != null) {
            NodeContext nc = buildNextNodeContext(null);
            nextNode.execute(nc);
        }*/
    }
}
