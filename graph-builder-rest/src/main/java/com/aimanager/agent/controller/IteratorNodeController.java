package com.aimanager.agent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.aimanager.agent.Form.CreateIteratorNodeForm;
import com.aimanager.agent.dto.IteratorNodeDto;
import com.aimanager.agent.models.IteratorNode;
import com.aimanager.agent.services.IteratorNodeService;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.Valid;


public interface IteratorNodeController extends IBaseController {

    static final Logger logger = LoggerFactory.getLogger(IteratorNodeController.class);

    @Autowired
    IteratorNodeService getIteratorNodeService();

    @Operation(summary = "Create an iterator node", description = "Create an iterator node with the given form. "+
            "The iterator node will be created in the graph with the given id.")
    @RequestMapping(value = "/iterator-node/create" , method = RequestMethod.POST)
    public default Response<IteratorNodeDto> createIteratorNode(@Valid @ModelAttribute CreateIteratorNodeForm form, BindingResult errors) {
        logger.info("Creating iterator node with form: {}", form);
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        IteratorNode iteratorNode = getIteratorNodeService().createIteratorNode(form);
        return Response.success(null,IteratorNodeDto.of(iteratorNode));
    }

    @Operation(summary = "Get an iterator node", description = "Get an iterator node with the given id.")
    @RequestMapping(value = "/iterators" , method = RequestMethod.GET)
    public default Response<IteratorNodeDto> getIteratorNode(
            @Parameter(required = true, description = "The id of the iterator node") @RequestParam Long id,
            @Parameter(required = true, description = "The id of the graph") @RequestParam Long graphId) {
        IteratorNode iteratorNode = getIteratorNodeService().getIteratorNodeById(id, graphId);
        return Response.success(null,IteratorNodeDto.of(iteratorNode));
    }

}
