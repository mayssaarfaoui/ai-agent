package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class GraphForm {

    @Parameter(required = true, description = "The graph's ID.")
    @NotNull(message = "Graph id is required")
    private Long graphId;

}
