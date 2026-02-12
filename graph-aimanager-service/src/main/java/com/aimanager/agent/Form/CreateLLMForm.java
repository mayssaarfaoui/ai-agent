package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;
import org.springdoc.api.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateLLMForm extends GraphForm {

    @Parameter(required = true, description = "The prompt of the LLM node")
    @NotNull(message = "Prompt is required")
    private String prompt;
}
