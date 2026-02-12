package com.aimanager.agent.controller;

import com.aimanager.agent.Form.answers.CreateDateAnswerForm;
import com.aimanager.agent.Form.answers.CreateFileAnswerForm;
import com.aimanager.agent.Form.answers.CreateNumberAnswerForm;
import com.aimanager.agent.Form.answers.CreateTextAnswerForm;
import com.aimanager.agent.Form.answers.EditDateAnswerForm;
import com.aimanager.agent.Form.answers.EditFileAnswerForm;
import com.aimanager.agent.Form.answers.EditNumberAnswerForm;
import com.aimanager.agent.Form.answers.EditTextAnswerForm;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aimanager.agent.dto.AnswerNodeDto;
import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.DateAnswer;
import com.aimanager.agent.models.FileAnswer;
import com.aimanager.agent.models.NumberAnswer;
import com.aimanager.agent.models.TextAnswer;
import com.aimanager.agent.services.AnswerNodeService;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;

public interface IAnswerController<T extends AnswerNode> extends IBaseController {

    public AnswerNodeService<T> getAnswerNodeService();

    @RequestMapping(value = "/answers", method = RequestMethod.GET)
    @Operation(summary = "Search answers by question ID and graph ID.")
    public default Response<RPage<AnswerNodeDto>> searchAnswers(
            @Parameter(description = "ID of the question", required = true) @RequestParam Long questionId,
            @Parameter(description = "ID of the graph", required = true) @RequestParam Long graphId,
            @ModelAttribute PageParams pageParams) {

        Page<T> answers = getAnswerNodeService().findAnswersByQuestionId(graphId, questionId, pageParams.getPageable());
        RPage<AnswerNodeDto> rPage = RPage.of(answers, AnswerNodeDto::of);
        return Response.success(null, rPage);
    }

    @RequestMapping(value = "/answers/get" , method = RequestMethod.GET)
    @Operation(summary = "Get answer by ID.")
    public default Response<AnswerNodeDto> getAnswerById(
            @Parameter(description = "ID of the graph", required = true) @RequestParam Long graphId,
            @Parameter(description = "ID of the question", required = true) @RequestParam Long questionId,
            @Parameter(description = "ID of the answer", required = true) @RequestParam Long id) {
        T answer = getAnswerNodeService().getAnswerNode(id, questionId, graphId);
        return Response.success(null, AnswerNodeDto.of(answer));
    }

   /* @RequestMapping(value = "/answers/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Update answer's text.", description = "Update the answer's text for a given answer node id. "+
            "The answer's text must be unique for the question. The answer's text can't be updated if the question is a yes/no question.")
    public default Response<AnswerNodeDto> updateAnswerById(@Valid @ModelAttribute UpdateAnswerNodeForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        AnswerNode answer = getAnswerNodeService().updateAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }*/

    @RequestMapping(value = "/answers/text-answer/create" , method = RequestMethod.POST)
    @Operation(summary = "Create text answer.", description = "Create a new text answer for a given question id and graph id.")
    public default Response<AnswerNodeDto> createAnswer(@Valid @ModelAttribute CreateTextAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        TextAnswer answer = getAnswerNodeService().createTextAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    } 

    @RequestMapping(value = "/answers/number-answer/create" , method = RequestMethod.POST)
    @Operation(summary = "Create number answer.", description = "Create a new number answer for a given question id and graph id.")
    public default Response<AnswerNodeDto> createAnswer(@Valid @ModelAttribute CreateNumberAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        NumberAnswer answer = getAnswerNodeService().createNumberAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }

    @RequestMapping(value = "/answers/file-answer/create" , method = RequestMethod.POST)
    @Operation(summary = "Create file answer.", description = "Create a new file answer for a given question id and graph id.",
    parameters = {@Parameter(description = "attachment files (max size per file is 5M and 10 files are allowed.)",
    name = "file",
    in = ParameterIn.QUERY,
    style = ParameterStyle.FORM,
    content = @Content(
            mediaType = "multipart/form-data",
            schema = @Schema(implementation = MultipartFile.class),
            encoding = @Encoding(name = "file", contentType = "*/*")
    )
)
})
    public default Response<AnswerNodeDto> createAnswer(@Valid @ModelAttribute CreateFileAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        FileAnswer answer = getAnswerNodeService().createFileAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }   

    @RequestMapping(value = "/answers/date-answer/create" , method = RequestMethod.POST)
    @Operation(summary = "Create date answer.", description = "Create a new date answer for a given question id and graph id.")
    public default Response<AnswerNodeDto> createAnswer(@Valid @ModelAttribute CreateDateAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        DateAnswer answer = getAnswerNodeService().createDateAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }

    @RequestMapping(value = "/answer/text-answer/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Edit answer.", description = "Edit an answer for a given answer id.")
    public default Response<AnswerNodeDto> editAnswer(@Valid @ModelAttribute EditTextAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        TextAnswer answer = getAnswerNodeService().updateTextAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }


    @RequestMapping(value = "/answer/number-answer/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Edit answer.", description = "Edit an answer for a given answer id.")
    public default Response<AnswerNodeDto> editAnswer(@Valid @ModelAttribute EditNumberAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        NumberAnswer answer = getAnswerNodeService().updateNumberAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }


    @RequestMapping(value = "/answer/file-answer/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Edit answer.", description = "Edit an answer for a given answer id.")
    public default Response<AnswerNodeDto> editAnswer(@Valid @ModelAttribute EditFileAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        FileAnswer answer = getAnswerNodeService().updateFileAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }

    @RequestMapping(value = "/answer/date-answer/edit" , method = RequestMethod.PUT)
    @Operation(summary = "Edit answer.", description = "Edit an answer for a given answer id.")
    public default Response<AnswerNodeDto> editAnswer(@Valid @ModelAttribute EditDateAnswerForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            Response.throwError(getOneErrorMessage(errors));
        }
        DateAnswer answer = getAnswerNodeService().updateDateAnswerNode(form);
        return Response.success(null, AnswerNodeDto.of(answer));
    }


    
}