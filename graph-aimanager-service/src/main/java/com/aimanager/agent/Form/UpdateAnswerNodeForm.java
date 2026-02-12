package com.aimanager.agent.Form;

import javax.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
@ParameterObject
@Getter
@Setter
public class UpdateAnswerNodeForm extends GraphForm{

    @Parameter(required = true, description = "The answer's Node ID.")
    @NotEmpty(message = "Answer's node ID is required")
    private Long answerId;

    @Parameter(required = true, description = "The answer's text.")
    @NotEmpty(message = "Answer's text is required")
    private String answer;
}