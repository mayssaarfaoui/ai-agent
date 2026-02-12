package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateGraphForm {

    @Parameter(required = true, description = "The name of the graph")
    @NotNull(message = "Name is required")
    private String name;

    @Parameter(required = true, description = "The description of the graph")
    @NotNull(message = "Description is required")
    private String description;

}
