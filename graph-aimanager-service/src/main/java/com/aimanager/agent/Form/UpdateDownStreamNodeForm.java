package com.aimanager.agent.Form;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotEmpty;

@ParameterObject
@Getter
@Setter
public class UpdateDownStreamNodeForm extends CreateDownStreamNodeForm{

    @Parameter(required = true, description = "The DownStream node's ID.")
    @NotEmpty(message = "DownStream node's ID is required")
    private Long nodeId;
}