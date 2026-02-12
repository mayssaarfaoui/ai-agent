package com.aimanager.agent.models;


import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.repositories.GraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class IAGraph {

    private static final Logger logger = LoggerFactory.getLogger(IAGraph.class);

    @Autowired
    TaskDownStream taskDownStream;

    @Autowired
    UserDownStream userDownStream;

    private Map<Long, GraphNode> nodes;

    @Autowired
    GraphNodeRepository graphNodeRepository;

    @Autowired
    GraphRepository graphRepository;

    private StartNode startNode;

    public GraphEntity getGraph(Long graphId){
        return graphRepository.findById(graphId).orElseThrow(
            () -> new RuntimeException("Graph not found"));
    }

    public void loadGraphNodes(Long graphId){
        GraphEntity graph = getGraph(graphId);
        List<GraphNode> nodes = graphNodeRepository.findAllByGraph(graph);
        startNode = (StartNode) nodes.stream().filter(node -> node.getType() == NodeType.START).findFirst().orElseThrow(
            () -> new RuntimeException("Start node not found"));
    }

    public void initiateGraph(){
        // set iterator for each downstream node
        /*userDownStream.setIteratorNode(usersIterator);
        taskDownStream.setIteratorNode(tasksIterator);
        // set order between nodes
        fetchUsersService.setNextNode(userDownStream);
        userDownStream.setNextNode(fetchTasksService);
        fetchTasksService.setNextNode(taskDownStream);

        // define the start node for the graph
        this.startNode = fetchUsersService;*/
        Long graphId = (Long) SharedGraphData.getData("graphId");
        loadGraphNodes(graphId);
    }

    public void run(NodeContext context){
       // startNode.execute(context);
       startNode.process(context);
    }

    public void printGraphInfos(){
        logger.info("--------------- Graph nodes infos : ------");
        logger.info(userDownStream.getNodeInfo());
        logger.info(taskDownStream.getNodeInfo());
        logger.info("--------------------------------------------");
    }
}
