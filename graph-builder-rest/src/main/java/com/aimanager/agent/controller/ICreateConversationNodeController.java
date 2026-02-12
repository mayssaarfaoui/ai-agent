package com.aimanager.agent.controller;

import com.aimanager.agent.Form.CreateConversationForm;
import com.aimanager.agent.Form.UpdateConversationNodeForm;
import com.aimanager.agent.dto.CreateConversationDto;
import com.aimanager.agent.models.CreateConversationNode;
import com.aimanager.agent.services.CreateConversationNodeService;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


public interface ICreateConversationNodeController extends IBaseController{

    public CreateConversationNodeService getCreateConversationNodeService();


    /**
     * Get all conversation nodes.
     *
     * @param pageParams the pagination parameters
     * @return a response containing a paginated list of conversation nodes

     */

    @GetMapping("/create-conversation-node")
    @Operation(summary = "Get all conversation nodes", description = "This endpoint retrieves all conversation nodes in the system.")
    public default Response<RPage<CreateConversationDto>> getAllConversationNodes(@ModelAttribute PageParams pageParams) {
        Page<CreateConversationNode> conversationNodes = getCreateConversationNodeService().getAllConversationNodes(pageParams.getPageable());
        RPage<CreateConversationDto> rPage = RPage.of(conversationNodes, CreateConversationDto::of);
        return Response.success(null, rPage);
    }

    /**
     * Create a new conversation node.
     *
     * @param form the form containing the details of the conversation node to be created
     * @return a response containing the created conversation node
     */

    @PostMapping("/create-conversation-node/create")
    @Operation(summary = "Create a new conversation node", description = "This endpoint allows you to create a new conversation node in the system.")
    public default Response<CreateConversationDto> createConversationNode(@ModelAttribute CreateConversationForm form) {
        CreateConversationNode createdNode = getCreateConversationNodeService().createConversationNode(form);
        return Response.success(null, CreateConversationDto.of(createdNode));
    }

    @PutMapping("/create-conversation-node/edit")
    @Operation(summary = "Edit an existing conversation node", description = "This endpoint allows you to edit an existing conversation node in the system.")
    public default Response<CreateConversationDto> editConversationNode(@ModelAttribute UpdateConversationNodeForm form) {
        CreateConversationNode updatedNode = getCreateConversationNodeService().editConversationNode(form);
        return Response.success(null, CreateConversationDto.of(updatedNode));
    }

    @GetMapping("/create-conversation-node/get")
    @Operation(summary = "Get a conversation node by ID", description = "This endpoint retrieves a conversation node by its ID.")
    public default Response<CreateConversationDto> getConversationNodeById(@Parameter(description = "The create conversation's ID. ") @RequestParam Long id) {
        CreateConversationNode node = getCreateConversationNodeService().getConversationNodeById(id);
        return Response.success(null, CreateConversationDto.of(node));
    }
}
