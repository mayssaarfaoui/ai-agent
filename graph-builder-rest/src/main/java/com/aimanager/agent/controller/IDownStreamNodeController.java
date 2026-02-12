package com.aimanager.agent.controller;

import com.aimanager.agent.Form.*;
import com.aimanager.agent.dto.DownStreamDto;
import com.aimanager.agent.dto.QuestionNodeDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.DownStreamNode;
import com.aimanager.agent.services.DownStreamNodeService;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface IDownStreamNodeController extends IBaseController {


    public abstract DownStreamNodeService getDownStreamNodeService();

    @Operation(summary = "Create a DownStream node.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DownStream node created successfully",
                    content = @Content(schema = @Schema(implementation = DownStreamDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/down-stream/create" , method = RequestMethod.POST)
    public default Response<DownStreamDto> createDownStreamNode(
            @Parameter(description = "Form data for creating a DownStream node")
            @ModelAttribute CreateDownStreamNodeForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        DownStreamNode node =  getDownStreamNodeService().createDownStreamNode(form);
        return Response.success(null,DownStreamDto.of(node));
    }


    @Operation(summary = "Edit a DownStream node", description = "Update the details of an existing DownStream node."
    +            " Provide the node ID and the new details in the request.")
    @RequestMapping(value = "/down-stream/edit" , method = RequestMethod.PUT)
    public default Response<DownStreamDto> editQuestion(@Valid @ModelAttribute UpdateDownStreamNodeForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        DownStreamNode node =  getDownStreamNodeService().updateDownStreamNode(form);
        return Response.success(null,DownStreamDto.of(node));
    }



    @Operation(summary = "Get a DownStream node by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DownStream node retrieved successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "404", description = "Question node not found")
    })
    @RequestMapping(value = "/down-stream/get" , method = RequestMethod.GET)
    public default Response<DownStreamDto> getDownStreamById(
            @Parameter(description = "ID of the graph containing the question", required = true)
            @RequestParam Long graphId,
            @Parameter(description = "ID of the question to retrieve", required = true)
            @RequestParam Long nodeId) throws MissingEntityException {
        DownStreamNode node = getDownStreamNodeService().getDownStreamNodeById(graphId, nodeId);
        return Response.success(null,DownStreamDto.of(node));
    }

    @Operation(summary = "Get paginated DownStream nodes for a specific graph")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DownStream nodes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Graph not found")
    })
    @RequestMapping(value = "/down-stream" , method = RequestMethod.GET)
    public default Response<RPage<DownStreamDto>> getDownStreamNodesPaginated(
            @Parameter(description = "ID of the graph", required = true)
            @RequestParam Long graphId,
            @ModelAttribute PageParams pageParams) throws MissingEntityException {
        Page<DownStreamNode> nodes = getDownStreamNodeService().getDownStreamNodesPaginated(graphId, pageParams.getPageable());
        RPage<DownStreamDto> rp =RPage.of(nodes,DownStreamDto::of);
        return Response.success(null,rp);
    }

}
