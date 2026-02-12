package com.aimanager.agent.controller;

import com.aimanager.agent.Form.CreateFetchDataNodeForm;
import com.aimanager.agent.dto.FetchDataNodeDto;
import com.aimanager.agent.models.FetchDataNode;
import com.aimanager.agent.models.FetchableType;
import com.aimanager.agent.models.FetchedResponseType;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.services.FetchDataNodeService;

import javax.validation.Valid;

public interface IFetchDataNodeController<GN extends GraphNode> extends IBaseController{

    static final Logger logger = LoggerFactory.getLogger(IFetchDataNodeController.class);


    public FetchDataNodeService<GN> getFetchDataNodeService();

    /*@RequestMapping(method = RequestMethod.GET)
    @Operation(summary = "Get a fetch node by id")
    public Response<RPage<NodeDto>> getFetchNodeById(
    @Parameter(required = true, description = "The graph's id") @RequestParam Long graphId,
    @ModelAttribute PageParams pageParams) {
        logger.info("Getting fetch node by id with page params: {}", pageParams);
        Page<FetchDataNode> fetchDataNodes = getFetchDataNodeService().getFetchNodesByGraphId(graphId, pageParams.getPageable());
        RPage<FetchDataNodeDto> rPage = RPage.of(fetchDataNodes,FetchDataNodeDto::of);
        return Response.success(null,rPage);    
    }*/

    @RequestMapping(value = "/fetch-node/create" , method = RequestMethod.POST)
    @Operation(summary = "Create a new fetch node")
    public default Response<FetchDataNodeDto> createFetchNode(
            @Parameter(required = true, description = "The graph's id") @RequestParam Long graphId,
            @Parameter(required = true, description = "The fetchable data's type.") @RequestParam FetchableType fetchableType,
            @Parameter(required = true, description = "The response's type.") @RequestParam FetchedResponseType responseType,
            @Valid @RequestBody CreateFetchDataNodeForm form, BindingResult errors) {
        logger.info("Creating fetch node with form: {}", form);
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        FetchDataNode fetchDataNode = getFetchDataNodeService().createFetchNode(graphId, form, responseType,fetchableType);
        return Response.success(null,FetchDataNodeDto.of(fetchDataNode));
    }

    @RequestMapping(value = "/fetch-node/get" , method = RequestMethod.GET)
    @Operation(summary = "Get a fetch node by id.")
    public default Response<FetchDataNodeDto> getFetchNodeById(
            @Parameter(required = true, description = "The graph's id") @RequestParam Long graphId,
            @Parameter(required = true, description = "The fetch node's id") @RequestParam Long id) {
        FetchDataNode fetchDataNode = getFetchDataNodeService().getFetchNodeByIdAndGraphId(id, graphId);
        return Response.success(null,FetchDataNodeDto.of(fetchDataNode));
    }

    @RequestMapping(value = "/fetch-node/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Edit fetch node")
    public default Response<FetchDataNodeDto> editFetchNode(
            @Parameter(required = true, description = "The graph's id") @RequestParam Long graphId,
            @Parameter(required = true, description = "The node's id") @RequestParam Long nodeId,
            @Parameter(required = true, description = "The fetchable data's type.") @RequestParam FetchableType fetchableType,
            @Parameter(required = true, description = "The response's type.") @RequestParam FetchedResponseType responseType,
            @Valid @RequestBody CreateFetchDataNodeForm form, BindingResult errors) {
        logger.info("Creating fetch node with form: {}", form);
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        FetchDataNode fetchDataNode = getFetchDataNodeService().editFetchNode(graphId, nodeId, form, responseType,fetchableType);
        return Response.success(null,FetchDataNodeDto.of(fetchDataNode));
    }
}
