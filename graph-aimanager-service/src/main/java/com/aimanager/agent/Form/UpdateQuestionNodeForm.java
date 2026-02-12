package com.aimanager.agent.Form;

import javax.validation.constraints.NotEmpty;

import org.springdoc.api.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class UpdateQuestionNodeForm extends GraphForm{

    @Parameter(required = true, description = "The question's Node ID.")
    @NotEmpty(message = "Question's node ID is required")
    private Long questionId;

    @Parameter(required = true, description = "The question's text.")
    @NotEmpty(message = "Question's text is required")
    private String question;
}