package com.aimanager.agent.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@ParameterObject
@Getter
@Setter
public class ContinueConversationForm extends UserConversationForm {

    @Parameter(required = true, description = "The id of the node to traverse")
    private Long nodeId;

    //@Parameter(required = true, description = "The answer to the question")
    //@NotBlank(message = "Answer is required")
    @JsonIgnore
    private String answer;
}
