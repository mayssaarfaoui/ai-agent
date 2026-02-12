package com.aimanager.agent.Form.answers;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class CreateFileAnswerForm extends CreateAnswerNodeForm {

    @Parameter(required = true, description = "The answer's file.")
    @NotNull(message = "Answer's file is required")
    private MultipartFile file;
}