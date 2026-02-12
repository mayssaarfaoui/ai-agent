package com.aimanager.agent.controller;

import com.aimanager.agent.Form.GraphForm;
import com.aimanager.agent.dto.NotificationNodeDto;
import com.aimanager.agent.models.NotificationNode;
import com.aimanager.agent.services.NotificationNodeService;
import com.aimanager.agent.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


public interface INotificationNodeController extends IBaseController {

   public abstract NotificationNodeService getNotificationNodeService();
    
   @Operation(summary = "Create a notification node")
   @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Notification node created successfully",
            content = @Content(schema = @Schema(implementation = NotificationNodeDto.class))),
    @ApiResponse(responseCode = "400", description = "Invalid input")
})
   @RequestMapping(value = "/notifications/create" , method = RequestMethod.POST)
   public default Response<NotificationNodeDto> createNotificationNode(@ModelAttribute GraphForm form, BindingResult errors) {
      if (errors.hasErrors()) {
         Response.throwError(getOneErrorMessage(errors));
      }
      NotificationNode node = getNotificationNodeService().createNode(form);
      return Response.success(null,NotificationNodeDto.of(node));
   }

   @Operation(summary = "Get a notification node by its ID")
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Notification node retrieved successfully",
         content = @Content(schema = @Schema(implementation = NotificationNodeDto.class)))
   })
   @RequestMapping(value = "/notifications/get" , method = RequestMethod.GET)
   public default Response<NotificationNodeDto> getNotificationNodeById(
      @Parameter(description = "ID of the graph containing the notification node", required = true)
      @RequestParam Long graphId,
      @Parameter(description = "ID of the node to retrieve", required = true)
      @RequestParam Long nodeId) {
      NotificationNode node = getNotificationNodeService().getNodeById(graphId, nodeId);
      return Response.success(null,NotificationNodeDto.of(node));
   }
}
