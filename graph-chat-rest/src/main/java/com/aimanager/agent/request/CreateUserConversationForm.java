package com.aimanager.agent.request;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateUserConversationForm extends UploadGraphForm{

    @Parameter(required = true, description = "The user id")
    @NotNull(message = "User id is required")
    private Long userId;
}
