package com.aimanager.agent.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.aimanager.agent.services.GoogleTaxonomyNodeService;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.aimanager.agent.Form.GraphForm;
import com.aimanager.agent.dto.GoogleTaxonomyNodeDto;
import com.aimanager.agent.models.GoogleTaxonomyNode;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


public interface IGoogleTaxonomyNodeController extends IBaseController {

    public GoogleTaxonomyNodeService getGoogleTaxonomyNodeService();

    @Operation(summary = "Create a Google Taxonomy Node", description = "Create a Google Taxonomy Node")
    @RequestMapping(value = "/google-taxonomy-node/create", method = RequestMethod.POST)
    public default  Response<GoogleTaxonomyNodeDto> createGoogleTaxonomyNode(@ModelAttribute GraphForm form,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.error(getOneErrorMessage(bindingResult));
        }
        GoogleTaxonomyNode createdGoogleTaxonomyNode = getGoogleTaxonomyNodeService().createGoogleTaxonomyNode(form);
        return Response.success(null,GoogleTaxonomyNodeDto.of(createdGoogleTaxonomyNode));
    }

    @Operation(summary = "Get a Google Taxonomy Node", description = "Get a Google Taxonomy Node")
    @RequestMapping(value = "/google-taxonomy-node/get", method = RequestMethod.GET)
    public default  Response<GoogleTaxonomyNodeDto> getGoogleTaxonomyNode(
        @Parameter(description = "ID of the graph containing the Google Taxonomy Node", required = true)
            @RequestParam Long graphId,
        @Parameter(description = "ID of the Google Taxonomy Node to retrieve", required = true)
            @RequestParam Long googleTaxonomyNodeId
    ) {
        GoogleTaxonomyNode googleTaxonomyNode = getGoogleTaxonomyNodeService().getGoogleTaxonomyNodeById(graphId, googleTaxonomyNodeId);
        return Response.success(null,GoogleTaxonomyNodeDto.of(googleTaxonomyNode));
    }

}
