package com.aimanager.agent.controller;

import com.aimanager.agent.Form.UpdateQuestionNodeForm;
import com.aimanager.agent.services.QuestionNodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.aimanager.agent.dto.QuestionNodeDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;
import com.aimanager.agent.Form.CreateChoicesQuestionForm;
import com.aimanager.agent.Form.CreateQuestionForm;
import com.aimanager.agent.models.QuestionNode;
import javax.validation.Valid;

public interface IQuestionsController extends IBaseController {


    public abstract QuestionNodeService getQuestionNodeService();

    @Operation(summary = "Create a multiple choices question node.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node created successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/questions/multiple-choices/create" , method = RequestMethod.POST)
    public default Response<QuestionNodeDto> createMultiChoicesQuestion(
            @Parameter(description = "Form data for creating a single/multi choice question")
            @ModelAttribute CreateChoicesQuestionForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionNode question =  getQuestionNodeService().createMultipleChoicesQuestion(form);
        return Response.success(null,QuestionNodeDto.of(question));
    }

    @Operation(summary = "Create a single choice question node", description = "Create a single choice question node with the given form. "+
            "The question node will be created in the graph with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node created successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/questions/single-choice/create" , method = RequestMethod.POST)
    public default Response<QuestionNodeDto> createSingleChoiceQuestion(
            @Parameter(description = "Form data for creating a single/multi choice question")
            @ModelAttribute CreateChoicesQuestionForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionNode question =  getQuestionNodeService().createSingleChoiceQuestion(form);
        return Response.success(null,QuestionNodeDto.of(question));
    }

    @Operation(summary = "Create a yes/no question node", description = "Create a yes/no question node with the given form. "+
            "The question node will be created in the graph with the given id. "+
            "Two response nodes will be created with the text 'Yes' and 'No'. Answers node are not editable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node created successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @RequestMapping(value = "/questions/yes-no-question/create" , method = RequestMethod.POST)
    public default Response<QuestionNodeDto> createYesNoQuestion(
            @Parameter(description = "Form data for creating a yes/no question")
            @ModelAttribute CreateQuestionForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionNode question =  getQuestionNodeService().addYesNoQuestion(form);
        return Response.success(null,QuestionNodeDto.of(question));
    }


    @Operation(summary = "Edit a question node", description = "Edit a question node with the given form. "+
            "The question node will be edited in the graph with the given id.")
    @RequestMapping(value = "/questions/edit" , method = RequestMethod.PUT)
    public default Response<QuestionNodeDto> editQuestion(@Valid @ModelAttribute UpdateQuestionNodeForm form, BindingResult errors) throws MissingEntityException {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        QuestionNode question =  getQuestionNodeService().updateQuestion(form);
        return Response.success(null,QuestionNodeDto.of(question));
    }



    @Operation(summary = "Get a question node by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question node retrieved successfully",
                    content = @Content(schema = @Schema(implementation = QuestionNodeDto.class))),
            @ApiResponse(responseCode = "404", description = "Question node not found")
    })
    @RequestMapping(value = "/questions/get" , method = RequestMethod.GET)
    public default Response<QuestionNodeDto> getQuestionById(
            @Parameter(description = "ID of the graph containing the question", required = true)
            @RequestParam Long graphId,
            @Parameter(description = "ID of the question to retrieve", required = true)
            @RequestParam Long questionId) throws MissingEntityException {
        QuestionNode question = getQuestionNodeService().getQuestionById(graphId, questionId);
        return Response.success(null,QuestionNodeDto.of(question));
    }

    @Operation(summary = "Get questions with pagination for a specific graph")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Graph not found")
    })
    @RequestMapping(value = "/questions" , method = RequestMethod.GET)
    public default Response<RPage<QuestionNodeDto>> getQuestionsPaginated(
            @Parameter(description = "ID of the graph", required = true)
            @RequestParam Long graphId,
            @ModelAttribute PageParams pageParams) throws MissingEntityException {
        Page<QuestionNode> questions = getQuestionNodeService().getQuestionsPaginated(graphId, pageParams.getPageable());
        RPage<QuestionNodeDto> rp =RPage.of(questions,QuestionNodeDto::of);
        return Response.success(null,rp);
    }

}
