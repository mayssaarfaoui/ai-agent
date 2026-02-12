package com.aimanager.agent.request;


import com.aimanager.agent.utils.PageParams;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@ParameterObject
@Getter
@Setter
public class ConversationSearchForm extends PageParams {

    @Parameter(required = true, description = "The User id")
    private Long userId;
}
