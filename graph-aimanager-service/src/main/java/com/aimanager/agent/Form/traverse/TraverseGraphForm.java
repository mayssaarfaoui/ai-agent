package com.aimanager.agent.Form.traverse;

import javax.validation.constraints.NotBlank;

import org.springdoc.api.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ParameterObject
public class TraverseGraphForm extends VisitGraphForm {

    @Parameter(required = true, description = "The id of the node to traverse")
    private Long nodeId;

    @Parameter(required = true, description = "The answer to the question")
    @NotBlank(message = "Answer is required")
    private String answer;

}
