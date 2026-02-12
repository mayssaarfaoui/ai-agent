package com.aimanager.agent.services;

import com.aimanager.agent.form.RequestForm;
import com.aimanager.agent.models.IAGraph;
import com.aimanager.agent.models.SharedGraphData;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.request.FetchedDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GraphNodesServices {

    private static final Logger logger = LoggerFactory.getLogger(GraphNodesServices.class);

    @Autowired
    IAGraph graph;

    public NodeContext getFirstNodeContext(Long organizationId){
        RequestForm  form = new RequestForm();
        Map<String,String> params = new HashMap<>();
        params.put("organizationId",organizationId.toString());
        form.setQueryParams(params);
        NodeContext nc = new NodeContext();
        nc.set("requestform",form);
        return nc;
    }

    public void processData(Long graphId, Long organizationId){
        logger.info("Processing organization with id : {} using graph with id : {}",organizationId, graphId);
        SharedGraphData.storeData("graphId", graphId);
        SharedGraphData.storeData("organizationId", organizationId);
        graph.initiateGraph();
        graph.printGraphInfos();
        NodeContext context = getFirstNodeContext(organizationId);
        graph.run(context);
    }

}
