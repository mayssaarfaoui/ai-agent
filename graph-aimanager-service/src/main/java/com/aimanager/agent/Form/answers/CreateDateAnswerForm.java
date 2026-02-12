package com.aimanager.agent.Form.answers;

import java.util.Calendar;
import java.util.Date;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
@ParameterObject
@Getter
@Setter
public class CreateDateAnswerForm extends CreateAnswerNodeForm {

    @Parameter(required = true, description = "Response's date. The input value should be in the following date format  : 2025-04-18T21:02:00")
    @NotEmpty(message = "Answer's date is required")
    private String answerDate;
}