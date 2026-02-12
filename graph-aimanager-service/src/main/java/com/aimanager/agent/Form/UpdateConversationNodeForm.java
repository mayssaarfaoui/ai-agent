package com.aimanager.agent.Form;

import com.aimanager.agent.models.CreateConversationNode;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotEmpty;

@ParameterObject
@Getter
@Setter
public class UpdateConversationNodeForm extends CreateConversationForm {

    @Parameter(required = true, description = "The node's ID.")
    @NotEmpty(message = "node ID is required")
    private Long nodeId;
}