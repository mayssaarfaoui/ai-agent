package com.aimanager.agent.controller;

import org.springframework.data.domain.Page;;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.aimanager.agent.dto.NodeDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.services.NodesConnectorService;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


public interface IEdgeController<N extends GraphNode>  {

    public NodesConnectorService<N> getNodesConnectorService();

    @RequestMapping(value = "/edge/connect", method = RequestMethod.PUT)
    @Operation(summary = "Connect two nodes in the graph.", 
               description = "Connect two nodes in the graph. The start node will be connected to the next node. "+
               "The edge will be created in the graph.")
    public default Response<?> setNextNode(
        @RequestParam @Parameter(description = "The graph's id") Long graphId,
        @RequestParam @Parameter(description = "The start node's id") Long startNodeId,
        @RequestParam @Parameter(description = "The next node's id") Long nextNodeId
    ) throws MissingEntityException {
        getNodesConnectorService().setNextNode(graphId, startNodeId,nextNodeId);
        return Response.success("nodes connected successfully",null);
    }

    @RequestMapping(value = "/edge/disconnect", method = RequestMethod.DELETE)
    @Operation(summary = "Disconnect two nodes in the graph.", 
               description = "Disconnect two nodes in the graph. The start node will be disconnected from the next node. "+
               "The edge will be removed from the graph.")
    public default Response<?> removeNextNode(
        @RequestParam @Parameter(description = "The graph's id") Long graphId,
        @RequestParam @Parameter(description = "The start node's id") Long startNodeId,
        @RequestParam @Parameter(description = "The next node's id") Long nextNodeId
    ) throws MissingEntityException {
        getNodesConnectorService().removeNodeConnection(graphId, startNodeId, nextNodeId);
        return Response.success("nodes disconnected successfully",null);
    }

    @RequestMapping(value = "/edge/connected-nodes", method = RequestMethod.GET)
    @Operation(summary = "Get paginated list of nodes connected to the specified node",
               description = "Returns a page of nodes that are connected to the specified node in the graph.")
    public default Response<RPage<NodeDto>> getConnectedNodes(
        @RequestParam @Parameter(description = "The graph's id") Long graphId,
        @RequestParam @Parameter(description = "The node's id") Long nodeId,
        @ModelAttribute PageParams pageParams) throws MissingEntityException {
        Page<N> connectedNodes = getNodesConnectorService().getConnectedNodes(graphId, nodeId, pageParams.getPageable());
        RPage<NodeDto> nodeDtos = RPage.of(connectedNodes, NodeDto::off);
        return Response.success(null, nodeDtos);
    }

}
