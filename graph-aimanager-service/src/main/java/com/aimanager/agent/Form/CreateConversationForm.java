package com.aimanager.agent.Form;

import com.aimanager.agent.models.ConversationType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotNull;

@ParameterObject
@Getter
@Setter
public class CreateConversationForm extends GraphForm {

    @NotNull
    @Parameter(description = "The conversation's type.", required = true)
    private ConversationType conversationType;

    @NotNull
    @Parameter(description = "The ID of the conversation graph to be created", required = true)
    private Long conversationGraphId;

    @NotNull
    @Parameter(description = "The ID of the conversation commit to be created", required = true)
    private Long conversationCommitId;
}
