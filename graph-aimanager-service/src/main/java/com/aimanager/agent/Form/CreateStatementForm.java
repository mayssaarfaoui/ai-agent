package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;
import org.springdoc.api.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateStatementForm extends GraphForm {

    @Parameter(required = true, description = "The statement text.")
    @NotNull(message = "Statement text is required")
    private String statement;
}
