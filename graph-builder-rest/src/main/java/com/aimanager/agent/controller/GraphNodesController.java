package com.aimanager.agent.controller;

import com.aimanager.agent.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aimanager.agent.Form.DeleteNodeForm;
import com.aimanager.agent.dto.NodeDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/nodes")
@CrossOrigin("http://localhost:3000")
public class GraphNodesController<N extends GraphNode> extends BaseController
implements IQuestionsController, IAnswerController, IEdgeController<N>,
        IFetchDataNodeController<N>, IteratorNodeController, IStatementController, ILLMNodeController, 
        IGoogleTaxonomyNodeController, ISubGraphController<N>, ICreateConversationNodeController,
        IQuestionsWithFreeFormController, IDownStreamNodeController, INotificationNodeController {

    private static final Logger logger = LoggerFactory.getLogger(GraphNodesController.class);

    @Autowired
    GraphNodesService<N> graphNodesService;

    @Autowired
    private AnswerNodeService answerNodeService;

    @Autowired
    FetchDataNodeService<N> fetchDataNodeService;

    @Autowired
    IteratorNodeService iteratorNodeService;

    @Autowired
    QuestionNodeService questionNodeService;

    @Autowired
    NodesConnectorService<N> nodesConnectorService;

    @Autowired
    StatementNodeService statementNodeService;

    @Autowired
    LLMNodeService llmNodeService;

    @Autowired
    GoogleTaxonomyNodeService googleTaxonomyNodeService;

    @Autowired
    SubGraphNodeService<N> subGraphNodeService;

    @Autowired
    CreateConversationNodeService createConversationNodeService;

    @Autowired
    QuestionWithFreeFormNodeService questionWithFreeFormNodeService;

    @Autowired
    DownStreamNodeService downStreamNodeService;

    @Autowired
    NotificationNodeService notificationNodeService;



    @Operation(summary = "Get start node", 
              description = "Retrieves the starting node of the graph")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved start node"),
        @ApiResponse(responseCode = "404", description = "Start node not found")
    })
    @RequestMapping(value = "/start-node/get", method = RequestMethod.GET)
    public Response<NodeDto> getStartNode(
        @RequestParam @Parameter(description = "The graph's id") Long graphId
    ) throws MissingEntityException {
        N node = graphNodesService.getStartNode(graphId);
        return Response.success(null, NodeDto.of(node));
    }

    @Operation(summary = "Get terminal node", 
              description = "Retrieves the terminal (end) node of the graph")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved terminal node"),
        @ApiResponse(responseCode = "404", description = "Terminal node not found")
    })
    @RequestMapping(value = "/terminal-node/get", method = RequestMethod.GET)
    public Response<NodeDto> getTerminalNode(
        @RequestParam @Parameter(description = "The graph's id") Long graphId
    ) throws MissingEntityException {
        N node = graphNodesService.getTerminalNode(graphId);
        return Response.success(null, NodeDto.of(node));
    }

    @Operation(summary = "Get graph nodes", 
              description = "Retrieves a paginated list of nodes belonging to a specific graph")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved nodes"),
        @ApiResponse(responseCode = "404", description = "Graph not found")
    })
    @GetMapping
    public Response<RPage<NodeDto>> getGraphNodes(
        @RequestParam @Parameter(description = "The graph's id") Long graphId,
        @ModelAttribute PageParams pageParams)throws MissingEntityException {
        Page<N> nodes = graphNodesService.getNodesByGraph(graphId, pageParams.getPageable());   
        return Response.success(null, RPage.of(nodes, NodeDto::of));
    }

    @Operation(summary = "Get node by id", 
    description = "Get a single node by id")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved node"),
        @ApiResponse(responseCode = "404", description = "Node not found")
    })
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Response<NodeDto> getNodeById(
        @RequestParam @Parameter(description = "The graph's id") Long graphId,
        @RequestParam @Parameter(description = "The node's id") Long id
    ) throws MissingEntityException {
        N node = graphNodesService.getNodeByIdAndGraph(graphId, id);
        return Response.success(null, NodeDto.of(node));
    }
    @Operation(summary = "Delete node", 
    description = "Delete a node by id. The node will be deleted from the graph and all its connections will be removed. "+
    "The start node can't be deleted. The terminal node can't be deleted.")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted node"),
        @ApiResponse(responseCode = "404", description = "Node not found")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Response<NodeDto> deleteNode(@ModelAttribute DeleteNodeForm form,BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        graphNodesService.deleteNode(form);
        return Response.success("Node deleted successfully");
    }

    @Override
    public QuestionNodeService getQuestionNodeService() {
        return questionNodeService;
    }

    @Override
    public AnswerNodeService getAnswerNodeService() {
        return answerNodeService;
    }

    @Override
    public NodesConnectorService<N> getNodesConnectorService() {
        return nodesConnectorService;
    }

    @Override
    public FetchDataNodeService<N> getFetchDataNodeService() {
        return fetchDataNodeService;
    }

    @Override
    public IteratorNodeService getIteratorNodeService() {
        return iteratorNodeService;
    }

    @Override
    public StatementNodeService getStatementNodeService() {
        return statementNodeService;
    }

    @Override
    public LLMNodeService getLLMNodeService() {
        return llmNodeService;
    }

    @Override
    public GoogleTaxonomyNodeService getGoogleTaxonomyNodeService() {
        return googleTaxonomyNodeService;
    }

    @Override
    public SubGraphNodeService<N> getSubGraphNodeService() {
        return subGraphNodeService;
    }

    @Override
    public CreateConversationNodeService getCreateConversationNodeService() {
        return createConversationNodeService;
    }

    @Override
    public QuestionWithFreeFormNodeService questionWithFreeFormNodeService() {
        return questionWithFreeFormNodeService;
    }

    @Override
    public DownStreamNodeService getDownStreamNodeService() {
        return downStreamNodeService;
    }

    @Override
    public NotificationNodeService getNotificationNodeService() {
        return notificationNodeService;
    }
}
