package com.aimanager.agent.Form.answers;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.NotEmpty;

@ParameterObject
@Getter
@Setter
public class EditNumberAnswerForm extends EditAnswerForm {

    @Parameter(required = true, description = "The answer's number.")
    @NotEmpty(message = "Answer's number is required")
    private Double answer;
}