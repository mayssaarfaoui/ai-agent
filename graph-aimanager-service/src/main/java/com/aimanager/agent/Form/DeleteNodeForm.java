package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.springdoc.api.annotations.ParameterObject;


import io.swagger.v3.oas.annotations.Parameter;

@ParameterObject
@Getter
@Setter
public class DeleteNodeForm extends GraphForm {

    @Parameter(description = "ID of the node to delete")
    @NotNull(message = "Node ID is required")
    private Long id;


}
