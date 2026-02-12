package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;
import org.springdoc.api.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateSubGraphForm extends GraphForm {

    @Parameter(required = true, description = "The id of the graph to be used as a sub graph")
    @NotNull(message = "Sub graph id is required")
    private Long subGraphId;

    @Parameter(required = true, description = "The commit id of the graph to be used as a sub graph")
    @NotNull(message = "Commit id is required")
    private Long commitId;
}
