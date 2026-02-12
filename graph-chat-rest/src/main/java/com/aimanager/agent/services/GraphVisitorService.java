package com.aimanager.agent.services;

import com.aimanager.agent.Form.traverse.TraverseGraphForm;
import com.aimanager.agent.dto.OptionDto;
import com.aimanager.agent.exceptions.MessageException;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.form.RequestForm;
import com.aimanager.agent.graph.node.Node;
import com.aimanager.agent.models.*;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.repositories.GraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphVisitorService<GN extends GraphNode> {

    private static final Logger logger = LoggerFactory.getLogger(GraphVisitorService.class);

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    GraphNodeRepository<GN> nodeRepository;

    @Autowired
    private ResponseForwarder responseForwarder;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Get a graph by its id
     *
     * @param id the id of the graph
     * @return the graph
     * @throws MissingEntityException if the graph is not found
     */
    public GraphEntity getGraph(Long id) throws MissingEntityException {
        logger.info("Getting graph with id: {}", id);
        return graphRepository.findById(id).orElseThrow(
                () -> new MissingEntityException("Graph with id : " + id + " not found"));
    }


    /*public GN getNode(Conversation conversation, Long nodeId) throws MissingEntityException {
        return nodeRepository.findById(nodeId).orElseThrow(() -> 
        new MissingEntityException("Node with id " + nodeId + " not found or has been deleted"));
    }*/


    public GN getNode(Conversation conversation, Long nodeId) throws MissingEntityException {
        GraphNode node = GraphLoaderService.getNode(conversation.getGraphId(), conversation.getCommitId(), nodeId);
        if (node == null) {
            throw new MissingEntityException("Node with id " + nodeId + " not found or has been deleted");
        }
        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), node);
        return (GN) node;
    }

    public void checkifNodeBelongsToGraph(GN node, GraphEntity graphEntity) throws MissingEntityException {
        logger.info("Checking if node with id {} belongs to graph with id {}", node.getId(), graphEntity.getId());
        if (!node.getGraph().getId().equals(graphEntity.getId())) {
            throw new MissingEntityException("Node with id " + node.getId() + " does not belong to graph with id " + graphEntity.getId());
        }
    }

    public GN getStartNode(GraphEntity graphEntity) throws MissingEntityException {
        logger.info("Getting start node for graph with id: {}", graphEntity.getId());
        return nodeRepository.findByGraphAndTypeAndStatus(graphEntity, NodeType.START, NodeStatus.ACTIVE).orElseThrow(() ->
                new MissingEntityException("Start node not found for graph with id " + graphEntity.getId()));
    }

   /* public GN visitGraph(VisitGraphForm form) throws MissingEntityException, MessageException {
        GraphEntity graphEntity = getGraph(form.getGraphId());
        GN node = getStartNode(graphEntity);
        checkifNodeBelongsToGraph(node, graphEntity);
       return processNode(node);
    }*/

    public GN getStartSubGraphNode(SubGraphNode node) throws MissingEntityException {
        StartNode startNode = GraphLoaderService.getStartNode(node.getSubGraphId(), node.getCommitId());
        List<GraphNode> connections = GraphLoaderService.getConnections(node.getSubGraphId(), node.getCommitId(), startNode);
        return (GN) connections.get(0);
    }

    protected GN moveToNextNode(Conversation conversation, GN node) throws MessageException, MissingEntityException {
        logger.info("Processing current node: {}", node.getId());
        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), node);
        GN nextNode = (GN) node.getConnections().get(0);
        if (nextNode == null) {
            throw new IllegalArgumentException("Next node is null");
        }
        GN responseNode = processNode(conversation, nextNode);
        return processNextNode(conversation, responseNode);
    }

    public GN processNode(Conversation conversation, GN node) throws MessageException, MissingEntityException {
        logger.info("Processing node with id: {}", node.getId());
        switch (node.getType()) {
            case START:
                return processStartNode(conversation, node);
            case END:
                return node;
            case QUESTION:
                return processQuestionAnswerNode(conversation, node);
            case ANSWER:
                return node;
            case GOOGLE_TAXONOMY:
                return node;
            case SUBGRAPH:
                return getStartSubGraphNode((SubGraphNode) node);
            case FETCH_DATA:
                return node;
            case STATEMENT:
                return node;
            case F_F_QUESTION:
                return node;
            case NOTIFICATION:
                return moveToNextNode(conversation, node);
            case CREATE_CONVERSATION:
                return moveToNextNode(conversation, node);
            default:
                throw new IllegalArgumentException("Invalid node type: " + node.getType());
        }
    }

    public GN processNextNode(Conversation conversation, GN nextNode) throws MessageException {
        logger.info("Processing next node: {}", nextNode.getId());
        if (nextNode == null) {
            throw new IllegalArgumentException("Next node is null");
        }
        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), nextNode);
        return nextNode;
    }

    protected GN processStartNode(Conversation conversation, GN node) throws MessageException, MissingEntityException {
        logger.info("Processing start node: {}", node.getId());
        StartNode startNode = (StartNode) node;
        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), startNode);
        GN nextNode = (GN) startNode.getConnections().get(0);
        if (nextNode == null) {
            throw new IllegalArgumentException("Next node is null");
        }
        GN responseNode = processNode(conversation, nextNode);
        return processNextNode(conversation, responseNode);
    }

    protected void processEndNode(Node node) throws MessageException {
        logger.info("Processing end node: {}", node.getId());
        throw new MessageException("Done processing this task.");
    }

    protected GN processQuestionAnswerNode(Conversation conversation, GN node) {
        logger.info("Processing question answer node: {}", node.getId());
        QuestionNode question = (QuestionNode) node;
        // Get the system or the user answer from the question
        String questionText = question.getQuestionText();
        if (questionText == null || questionText.isEmpty()) {
            throw new IllegalArgumentException("Question is null for question answer node with id " + node.getId());
        }
        return (GN) question;
    }

    public void handleUserAnswer(String userResponse, QuestionWithFreeFormNode node) {
        if (node.isSendResponse())
            responseForwarder.sendResponse(node, userResponse);
    }

    public GN processAnswer(Conversation conversation, GN node, String answer) {
        logger.info("Processing answer: {} for node with id: {}", answer, node.getId());

        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), node);

        if (node.getType() == NodeType.QUESTION) {
            QuestionNode questionNode = (QuestionNode) node;
            questionNode.setup();
            AnswerNode answerNode = (AnswerNode) questionNode.getBranchTable().get(answer.toLowerCase());
            GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), answerNode);
            return (GN) answerNode.getConnections().get(0);
        }

        if (node.getType() == NodeType.GOOGLE_TAXONOMY) {
            GoogleTaxonomyNode answerNode = (GoogleTaxonomyNode) node;
            NodeContext context = new NodeContext();
            context.set("parentId", answer);
            answerNode.process(context);
            if (answerNode.getTaxonomyElements() != null || answerNode.getTaxonomyElements().isEmpty()) {
                GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), answerNode);
                return (GN) answerNode;
            }
        }

        if (node.getType() == NodeType.F_F_QUESTION) {
            QuestionWithFreeFormNode question = (QuestionWithFreeFormNode) node;
            handleUserAnswer(answer, question);
        }

        GN answerNode = (GN) node.getConnections().get(0);

        GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), answerNode);

        if (answerNode == null) {
            throw new IllegalArgumentException("Answer node not found for answer: " + answer + " and " +
                    "question answer node with id :{" + node.getId() + "} .");
        }

        return answerNode;
    }

    public GN getWorkflowNextNode(Long graphId, Long commitId, GN node) {
        if (node == null) {
            throw new IllegalArgumentException("Please check the graph consistency, There is a node without connections.");
        }

        if (node.getType() == NodeType.END)
            return node;

        if (node.getType() != NodeType.FETCH_DATA && node.getType() != NodeType.ITERATOR && node.getType() != NodeType.CREATE_CONVERSATION
                && node.getType() != NodeType.SUBGRAPH && node.getType() != NodeType.NOTIFICATION) {
            return node;
        }

        List<GraphNode> connections = GraphLoaderService.getConnections(graphId, commitId, node);

        if (connections == null || connections.isEmpty()) {
            throw new IllegalArgumentException("Node with id " + node.getId() + " has no connections.");
        }
        if (connections.size() > 1) {
            throw new IllegalArgumentException("Node with id " + node.getId() + " has multiple connections. Please check the graph consistency.");
        }

        return getWorkflowNextNode(graphId, commitId, (GN) connections.get(0));
    }

    /*public Map<String, String> getFetchableData(String answer) {
        try {
            return objectMapper.readValue(answer, new TypeReference<Map<String, String>>() {
            });
        }catch (JsonProcessingException exception){
            throw new IllegalArgumentException("Required parameters are missing");
        }
    }*/

    public Map<String, String> getFetchableData(String answer) {
        try {
            List<OptionDto> p = objectMapper.readValue(answer, new TypeReference<List<OptionDto>>() {
            });
            Map<String, String> parameters = new HashMap<>();
            p.forEach(optionDto -> parameters.put(optionDto.getKey(), optionDto.getValue()));
            return parameters;
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Required parameters are missing");
        }
    }

    public NodeContext buildFectNodeContext(Long graphId, Long commitId, Map<String, String> parameters) {
        RequestForm form = new RequestForm();
        form.setQueryParams(parameters);
        NodeContext context = new NodeContext();
        context.set("requestform", form);
        context.set("graphId", graphId);
        context.set("commitId", commitId);
        return context;
    }

    public NodeContext saveSharedDataAndBuildContext(Conversation conversation, Map<String, String> parameters) {
        Long graphId = conversation.getGraphId();
        Long commitId = conversation.getCommitId();
        SharedGraphData.storeData("graphId", graphId);
        parameters.forEach((key, value) ->
                SharedGraphData.storeData(key, value));
        // build node context
        NodeContext context = buildFectNodeContext(graphId, commitId, parameters);
        return context;
    }

    public GN traverseFetchDataGraphNode(Conversation conversation, FetchDataNode node, String answer) {
        Map<String, String> parameters = getFetchableData(answer);
        // check how to build scpecefix contect for that pupose
        NodeContext context = saveSharedDataAndBuildContext(conversation, parameters);
        // run the fetch users node and the related workflow
        // the workflow will be executed by the node itself: fetch tasks for every user --> create conversation for each user/task
        node.process(context);
        // return the next node of the workflow
        return getWorkflowNextNode(conversation.getGraphId(), conversation.getCommitId(), (GN) node);
    }

    public GN traverseFetchGraphNode(Conversation conversation, GN node, String answer) throws MessageException, MissingEntityException {
        FetchDataNode fetchDataNode = (FetchDataNode) node;
        return traverseFetchDataGraphNode(conversation, fetchDataNode, answer);
    }

    public GN traverseGraph(Conversation conversation, GN node, String answer) throws MissingEntityException, MessageException {
        // GraphEntity graphEntity = getGraph(form.getGraphId());
        GN answerNode = processAnswer(conversation, node, answer);
        GN nextNode = processNode(conversation, answerNode);
        return processNextNode(conversation, nextNode);
    }

    public GN traverseGraph(Conversation conversation, TraverseGraphForm form) throws
            MissingEntityException, MessageException {
        // GraphEntity graphEntity = getGraph(form.getGraphId());
        GN node = getNode(conversation, form.getNodeId());
        //checkifNodeBelongsToGraph(node, graphEntity);
        switch (node.getType()) {
            case START:
            case END:
                throw new IllegalArgumentException("Start and End nodes cannot be traversed directly.");
            case FETCH_DATA:
                return traverseFetchGraphNode(conversation, node, form.getAnswer());
            default:
                return traverseGraph(conversation, node, form.getAnswer());
        }
    }

    public void printNode(GN node) {
        logger.info("Node id: {} and Type : {}.", node.getId(), node.getType());
    }
}
