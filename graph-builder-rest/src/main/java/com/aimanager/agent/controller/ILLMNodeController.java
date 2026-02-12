package com.aimanager.agent.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aimanager.agent.Form.CreateLLMForm;
import com.aimanager.agent.dto.LLMNodeDto;
import com.aimanager.agent.models.LLMNode;
import com.aimanager.agent.services.LLMNodeService;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

public interface ILLMNodeController extends IBaseController {

    public abstract LLMNodeService getLLMNodeService();

    @Operation(summary = "Create a LLM node")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "LLM node created successfully",
                content = @Content(schema = @Schema(implementation = LLMNodeDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
})
    @RequestMapping(value = "/llm-nodes/create", method = RequestMethod.POST)
    public default Response<LLMNode> createLLMNode(@ModelAttribute CreateLLMForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        LLMNode lLMNode = getLLMNodeService().createLLMNode(form);
        return Response.success(null, lLMNode);
    }


    @Operation(summary = "Get a LLM node by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "LLM node retrieved successfully",
                content = @Content(schema = @Schema(implementation = LLMNodeDto.class))),
        @ApiResponse(responseCode = "404", description = "LLM node not found")
    })
    @RequestMapping(value = "/llm-nodes/get", method = RequestMethod.GET)
    public default Response<LLMNodeDto> getLLMNodeById(
        @Parameter(description = "ID of the graph containing the LLM node", required = true)
            @RequestParam Long graphId,
        @Parameter(description = "ID of the LLM node to retrieve", required = true)
            @RequestParam Long llmNodeId
    ) {
        LLMNode lLMNode = getLLMNodeService().getLLMNodeById(graphId, llmNodeId);
        return Response.success(null, LLMNodeDto.of(lLMNode));
    }
    

}
