package com.aimanager.agent.Form.answers;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.NotEmpty;

@ParameterObject
@Getter
@Setter
public class EditTextAnswerForm extends EditAnswerForm {

    @Parameter(required = true, description = "The answer's text.")
    @NotEmpty(message = "Answer's text is required")
    private String answer;

}
