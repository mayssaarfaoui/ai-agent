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
public class EditAnswerForm extends GraphForm{

    @Parameter(required = true, description = "The answer's Node ID.")
    @NotEmpty(message = "Answer's node ID is required")
    private Long answerId;
    
    
}