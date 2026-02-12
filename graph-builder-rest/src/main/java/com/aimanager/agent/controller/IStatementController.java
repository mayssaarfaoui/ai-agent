package com.aimanager.agent.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;

import com.aimanager.agent.Form.CreateStatementForm;
import com.aimanager.agent.dto.StatementNodeDto;
import com.aimanager.agent.models.StatementNode;
import com.aimanager.agent.services.StatementNodeService;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;


public interface IStatementController extends IBaseController {

   public abstract StatementNodeService getStatementNodeService();
    
   @Operation(summary = "Create a statement node")
   @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Statement node created successfully",
            content = @Content(schema = @Schema(implementation = StatementNodeDto.class))),
    @ApiResponse(responseCode = "400", description = "Invalid input")
})
   @RequestMapping(value = "/statements/create" , method = RequestMethod.POST)
   public default Response<StatementNode> createStatementNode(@ModelAttribute CreateStatementForm form, BindingResult errors) {
      if (errors.hasErrors()) {
         Response.throwError(getOneErrorMessage(errors));
      }
      StatementNode statementNode = getStatementNodeService().createStatementNode(form);
      return Response.success(null,statementNode);
   }

   @Operation(summary = "Get a statement node by its ID")
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Statement node retrieved successfully",
         content = @Content(schema = @Schema(implementation = StatementNodeDto.class)))
   })
   @RequestMapping(value = "/statements/get" , method = RequestMethod.GET)
   public default Response<StatementNodeDto> getStatementNodeById(  
      @Parameter(description = "ID of the graph containing the statement", required = true)
      @RequestParam Long graphId,
      @Parameter(description = "ID of the statement to retrieve", required = true)
      @RequestParam Long statementId) {
      StatementNode statementNode = getStatementNodeService().getStatementNodeById(graphId, statementId);
      return Response.success(null,StatementNodeDto.of(statementNode));
   }
}
