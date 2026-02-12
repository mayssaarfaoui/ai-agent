package com.aimanager.agent.Form;

import javax.validation.constraints.NotNull;
import org.springdoc.api.annotations.ParameterObject;
import com.aimanager.agent.models.AnswerType;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateChoicesQuestionForm extends CreateQuestionForm {

    @Parameter(required = true, description = "The response type of the question")
    @NotNull(message = "Response type is required")
    private AnswerType responseType;

}
