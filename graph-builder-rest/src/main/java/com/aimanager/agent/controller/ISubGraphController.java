package com.aimanager.agent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

import com.aimanager.agent.Form.CreateSubGraphForm;
import com.aimanager.agent.dto.SubGraphNodeDto;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.SubGraphNode;
import com.aimanager.agent.services.SubGraphNodeService;
import com.aimanager.agent.utils.Response;
import org.springframework.validation.BindingResult;

public interface ISubGraphController<N extends GraphNode> extends IBaseController {

    public abstract SubGraphNodeService<N> getSubGraphNodeService();

    @Operation(summary = "Create a sub graph")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sub graph created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/sub-graph-node/create" , method = RequestMethod.POST)
    public default Response<SubGraphNodeDto> createSubGraph(@RequestBody CreateSubGraphForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        SubGraphNode subGraphNode = getSubGraphNodeService().createSubGraph(form);
        return Response.success(null, SubGraphNodeDto.of(subGraphNode));
    }

    @Operation(summary = "Get a sub graph node by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sub graph node found successfully"),
        @ApiResponse(responseCode = "404", description = "Sub graph node not found")
    })
    @RequestMapping(value = "/sub-graph-node/get" , method = RequestMethod.GET)
    public default Response<SubGraphNodeDto> getSubGraphNodeById(
        @Parameter(description = "ID of the graph containing the sub graph node", required = true)
        @RequestParam Long graphId,
        @Parameter(description = "ID of the sub graph node to retrieve", required = true)
        @RequestParam Long nodeId
    ) {
        SubGraphNode subGraphNode = getSubGraphNodeService().getSubGraphNodeById(graphId, nodeId);
        return Response.success(null, SubGraphNodeDto.of(subGraphNode));
    }
}
