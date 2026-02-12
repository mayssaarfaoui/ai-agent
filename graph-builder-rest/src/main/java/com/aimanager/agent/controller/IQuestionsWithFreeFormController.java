package com.aimanager.agent.controller;

import com.aimanager.agent.Form.*;
import com.aimanager.agent.dto.QuestionNodeDto;
import com.aimanager.agent.dto.QuestionWithFreeFormNodeDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.QuestionWithFreeFormNode;
import com.aimanager.agent.services.QuestionWithFreeFormNodeService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IQuestionsWithFreeFormController extends IBaseController {


    public abstract QuestionWithFreeFormNodeService questionWithFreeFormNodeService();


    @Operation(summary = "Create a free form question node", description = "Create a free form question node with the given form. "+
            "The question node will be created in the graph with the given id. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node created successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/free-form-questions/create" , method = RequestMethod.POST)
    public default Response<QuestionWithFreeFormNodeDto> createFreeFormQuestion(
            @Parameter(description = "Form data for creating a free form question")
            @ModelAttribute CreateQuestionWithFFForm form,
            @RequestBody ServiceDetailsForm service,
            BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionWithFreeFormNode question =  questionWithFreeFormNodeService().addQuestion(form, service);
        return Response.success(null,QuestionWithFreeFormNodeDto.of(question));
    }


    @Operation(summary = "Edit a question node", description = "Edit a question node with the given form. "+
            "The question node will be edited in the graph with the given id.")
    @RequestMapping(value = "/free-form-questions/edit" , method = RequestMethod.PUT)
    public default Response<QuestionWithFreeFormNodeDto> editFreeFormQuestion(@Valid @ModelAttribute UpdateQuestionWithFFForm form, @RequestBody ServiceDetailsForm service, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionWithFreeFormNode question =  questionWithFreeFormNodeService().updateQuestion(form, service);
        return Response.success(null,QuestionWithFreeFormNodeDto.of(question));
    }



    @Operation(summary = "Get a question node by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node retrieved successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "404", description = "Question node not found")
    })
    @RequestMapping(value = "/free-form-questions/get" , method = RequestMethod.GET)
    public default Response<QuestionWithFreeFormNodeDto> getFreeFormQuestionById(
            @Parameter(description = "ID of the graph containing the question", required = true)
            @RequestParam Long graphId,
            @Parameter(description = "ID of the question to retrieve", required = true)
            @RequestParam Long questionId) throws MissingEntityException {
        QuestionWithFreeFormNode question = questionWithFreeFormNodeService().getQuestionById(graphId, questionId);
        return Response.success(null,QuestionWithFreeFormNodeDto.of(question));
    }

    @Operation(summary = "Get questions with pagination for a specific graph")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Graph not found")
    })
    @RequestMapping(value = "/free-form-questions" , method = RequestMethod.GET)
    public default Response<RPage<QuestionWithFreeFormNodeDto>> getFreeFormQuestionsPaginated(
            @Parameter(description = "ID of the graph", required = true)
            @RequestParam Long graphId,
            @ModelAttribute PageParams pageParams) throws MissingEntityException {
        Page<QuestionWithFreeFormNode> questions = questionWithFreeFormNodeService().getQuestionsPaginated(graphId, pageParams.getPageable());
        RPage<QuestionWithFreeFormNodeDto> rp =RPage.of(questions,QuestionWithFreeFormNodeDto::of);
        return Response.success(null,rp);
    }

}
