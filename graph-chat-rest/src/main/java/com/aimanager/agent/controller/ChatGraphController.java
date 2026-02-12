package com.aimanager.agent.controller;


import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aimanager.agent.exceptions.MessageException;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.request.UploadGraphForm;
import com.aimanager.agent.services.ChatGraphService;
import com.aimanager.agent.services.GraphService;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import java.nio.file.Files;

@RestController
@RequestMapping("/chat-graph")
@CrossOrigin("http://localhost:3000")
public class ChatGraphController<GN extends GraphNode>   extends BaseController {

    @Autowired
    private ChatGraphService<GN> chatGraphService;

    @Autowired
    private GraphService<GN> graphService;

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
        return Response.success("Graph uploaded successfully", null);
    }

    @GetMapping("/display-graph")
    @Operation(summary = "Display graph used in for conversations.", 
              description = "Display a graph in PDF format and download it as a file. The graph's id and commit's id are required.")
    public ResponseEntity<Resource> displayGraph(
            @Parameter(description = "The graph's id") @RequestParam Long id,
            @Parameter(description = "The commit's id") @RequestParam Long commitId) 
            throws MissingEntityException, IOException   {
        
        String dotContent = chatGraphService.exportGraphToDot(id, commitId);
        File pdfFile = graphService.convertDotToPdf(dotContent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=graph-" + id + "-" + commitId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(Files.readAllBytes(pdfFile.toPath())));
    }
    
}