package com.aimanager.agent.Form;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotNull;

@ParameterObject
@Getter
@Setter
public class UpdateGraphForm extends CreateGraphForm {

    @Parameter(required = true, description = "The id of the graph")
    @NotNull(message = "Id is required")
    private Long id;

}
