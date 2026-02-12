package com.aimanager.agent.controller;


import com.aimanager.agent.Form.CreateGraphForm;
import com.aimanager.agent.dto.ConversationResponseDto;
import com.aimanager.agent.dto.conversation.ConversationDto;
import com.aimanager.agent.exceptions.MessageException;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.Message;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.models.conversation.UserConversation;
import com.aimanager.agent.request.ContinueConversationForm;
import com.aimanager.agent.request.ConversationSearchForm;
import com.aimanager.agent.request.CreateUserConversationForm;
import com.aimanager.agent.request.UploadGraphForm;
import com.aimanager.agent.request.UserConversationForm;
import com.aimanager.agent.services.ChatGraphService;
import com.aimanager.agent.services.ConversationService;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/conversations")
@CrossOrigin("http://localhost:3000")
public class ConversationController<N extends GraphNode,C extends Conversation,M extends Message> extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    ConversationService<N,C,M> conversationService;

    @Autowired
    ChatGraphService chatGraphService;

    @Operation(summary = "Delete User's conversations", description = "Delete all conversations of a user, the user's Id should be indicated.")
    @DeleteMapping("/clear-conversations")
    public Response clearConversations(@Parameter(description = "The user's id") @RequestParam Long id) throws MessageException, MissingEntityException {
        conversationService.clearConversation(id);
        return Response.success(null, "Conversation successfully deleted.");
    }

    @Operation(summary = "Delete Conversation", description = "Delete a conversation, the conversation's ID and the user's Id should be indicated.")
    @DeleteMapping("/delete")
    public Response deleteConversation(@ModelAttribute UserConversationForm form) throws MessageException, MissingEntityException {
       conversationService.deleteConversation(form);
        return Response.success(null, "Conversation successfully deleted.");
    }


    @Operation(summary = "Search Conversations", description = "Search Conversations by User Id. The user's id is required. " +
            "Pagination is optional, by default it returns the first 10 conversations.")
    @GetMapping
    public Response<RPage<ConversationDto>> searchConversations(@ModelAttribute ConversationSearchForm form) {
        logger.info("Searching conversations for user id: {} and page params :({},{}).", form.getUserId(), form.getPageNumber(), form.getPageSize());
        Page<C> conversations = conversationService.searchConversations(form);
        logger.info("Found {} conversations for user id: {}.", conversations.getTotalElements(), form.getUserId());
        RPage<ConversationDto> conversationRPage = RPage.of(conversations, ConversationDto::of);
        return Response.success(null, conversationRPage);
    }

    @Operation(summary = "Skip a Conversation", description = "Skip a conversation by its id. The conversation's id is required.")
    @PutMapping("/skip")
    public Response<?> skipConversation(@ModelAttribute UserConversationForm form) {
        conversationService.skipConversation(form);
        return Response.success(null, "Conversation skipped successfully");
    }

    @Operation(summary = "Close Conversation", description = "Close a conversation by its id. The conversation's id is required.")
    @PutMapping("/close")
    public Response<?> closeConversation(@ModelAttribute UserConversationForm form) {
        conversationService.closeConversation(form);
        return Response.success(null, "Conversation closed successfully");
    }

    @Operation(summary = "Start Conversation", description = "Start a conversation by its id. The conversation's id is required.")
    @PostMapping("/start")
    public Response<ConversationResponseDto> startConversation(@ModelAttribute UserConversationForm form) throws MessageException, MissingEntityException {
        ConversationResponseDto response = conversationService.startConversation(form);
        return Response.success(null, response);
    }

    @Operation(summary = "Continue Conversation", description = "Continue a conversation by its id. The conversation's id is required.")
    @PostMapping("/reply")
    public Response<ConversationResponseDto> continueConversation(@ModelAttribute ContinueConversationForm form,
                                                                  @RequestBody String answer) throws MessageException, MissingEntityException {
        ConversationResponseDto cResponse = conversationService.replyToConversation(form,answer);
        return Response.success(null, cResponse);
    }

    @Operation(summary = "Get Conversation", description = "Get a conversation by its id. The conversation's id is required.")
    @GetMapping("/get")
    public Response<ConversationDto> getConversation(@ModelAttribute UserConversationForm form) throws MessageException, MissingEntityException {
        C response = conversationService.getConversation(form);
        return Response.success(null, ConversationDto.of(response));
    }

    @Operation(summary = "Back Conversation", description = "Back a conversation by its id. This will remove the latest message from the conversation." +
            " The conversation's id is required.")
    @DeleteMapping("/back")
    public Response<?> backConversation(@ModelAttribute UserConversationForm form) throws MessageException, MissingEntityException {
        conversationService.backConversation(form);
        return Response.success(null, "latest message removed successfully.");
    }

    @Operation(summary = "Create User Conversation", description = "Create a user conversation by its id. " +
            "The user's id and graph's id are required.")
    @PostMapping("/create")
    public Response<ConversationDto> createUserConversation(@ModelAttribute CreateUserConversationForm form) throws MessageException, MissingEntityException {
        UserConversation conversation = conversationService.createUserConversation(form);
        return Response.success(null, ConversationDto.of(conversation));
    }

    @Deprecated
    @Operation(summary = "Upload Conversation Graph", description = "Upload a conversation graph by its id. The graph's id is required.",
    parameters = @Parameter(
        description = "File to upload",
        name = "file",
        in = ParameterIn.QUERY,
        style = ParameterStyle.FORM,
        content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = MultipartFile.class),
                encoding = @Encoding(name = "file", contentType = "application/json")
        )
))
    @PostMapping("/upload-graph")
    public Response<String> uploadConversationGraph(@ModelAttribute UploadGraphForm form,BindingResult bindingResult) throws MessageException, MissingEntityException {
        if (bindingResult.hasErrors()) {
            return Response.error(null, getOneErrorMessage(bindingResult));
        }
        chatGraphService.uploadConversationGraph(form);
        return Response.success(null, "Graph uploaded successfully");
    }


}
