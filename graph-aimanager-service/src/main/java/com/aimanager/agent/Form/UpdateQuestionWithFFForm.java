package com.aimanager.agent.Form;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotEmpty;

@ParameterObject
@Getter
@Setter
public class UpdateQuestionWithFFForm extends CreateQuestionWithFFForm{

    @Parameter(required = true, description = "The question's Node ID.")
    @NotEmpty(message = "Question's node ID is required")
    private Long questionId;
}