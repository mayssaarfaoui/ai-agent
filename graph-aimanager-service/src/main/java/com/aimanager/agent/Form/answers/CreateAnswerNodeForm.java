package com.aimanager.agent.Form.answers;

import javax.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import com.aimanager.agent.Form.GraphForm;
@ParameterObject
@Getter
@Setter
public class CreateAnswerNodeForm extends GraphForm{

    @Parameter(required = true, description = "The question's Node ID.")
    @NotEmpty(message = "Question's node ID is required")
    private Long questionId;
}