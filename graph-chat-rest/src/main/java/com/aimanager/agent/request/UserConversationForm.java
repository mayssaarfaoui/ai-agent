package com.aimanager.agent.request;


import com.aimanager.agent.utils.PageParams;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@ParameterObject
@Getter
@Setter
public class UserConversationForm {

    @Parameter(required = true,description = "The User id")
    private Long userId;

    @Parameter(required = true,description = "The Conversation id")
    private UUID conversationId;
}
