package com.aimanager.agent.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import com.aimanager.agent.Form.UpdateGraphForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aimanager.agent.Form.CreateGraphForm;
import com.aimanager.agent.dto.GraphEntityDto;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.services.GraphService;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/graph")
@CrossOrigin("http://localhost:3000")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @Operation(summary = "edit graph", description = "Edit a graph's name and description based on the given graph's ID.")
    @PutMapping("/edit")
    public Response editGraph(@ModelAttribute UpdateGraphForm form)
            throws MissingEntityException {
        GraphEntity graphEntity = graphService.editGraph(form);
        return Response.success("Graph edited successfully", GraphEntityDto.of(graphEntity));
    }

    @Operation(summary = "delete graph", description = "Delete a graph based on the given graph's ID." +
            " All graph commits and related nodes, will be deleted also")
    @DeleteMapping("/delete")
    public Response deleteGraph(@Parameter(description = "The graph's id") @RequestParam Long id)
            throws MissingEntityException {
        graphService.deleteGraph(id);
        return Response.success(null,"Graph deleted successfully");
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new graph", 
    description = "Create a new graph with the given name and description")
    public Response<GraphEntityDto> createGraph(@ModelAttribute CreateGraphForm form) {
        GraphEntity graphEntity = graphService.createGraph(form);
        return Response.success("Graph created successfully", GraphEntityDto.of(graphEntity));
    }

    @GetMapping("/get")
    @Operation(summary = "Get a graph by id", description = "Get a graph by id")
    public Response<GraphEntityDto> getGraph(
            @Parameter(description = "The graph's id") @RequestParam Long id)
            throws MissingEntityException {
        GraphEntity graphEntity = graphService.getGraph(id);
        return Response.success("Graph retrieved successfully", GraphEntityDto.of(graphEntity));
    }

    @RequestMapping(method = RequestMethod.GET)
    @Operation(summary = "Get all graphs", description = "Get all graphs")
    public Response<RPage<GraphEntityDto>> getGraphs(@ModelAttribute PageParams pageParams){
        Page<GraphEntity> graphEntities = graphService.getGraphs(pageParams.getPageable());
        RPage<GraphEntityDto> rPage = RPage.of(graphEntities, GraphEntityDto::of);
        return Response.success("Graphs retrieved successfully", rPage);
    }

    @GetMapping("/export")
    @Operation(summary = "Export graph as DOT file.", 
              description = "Export a graph in DOT format and download it as a file. The graph can be flattened or not. " +
              "If flatten is true, the graph will be flattened and the commit's id is required.")
    public ResponseEntity<Resource> exportGraph(
            @Parameter(description = "The graph's id") @RequestParam Long id,
            @Parameter(description = "The commit's id") @RequestParam Long commitId,
            @Parameter( required = false, description = "Flatten the graph") @RequestParam(required = false) Boolean flatten) 
            throws MissingEntityException {
        
        String dotContent = graphService.exportGraphToDot(id, commitId, flatten);
        
        // Create a ByteArrayResource from the DOT content
        ByteArrayResource resource = new ByteArrayResource(dotContent.getBytes());
        
        // Set up the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=graph.dot");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }


    @GetMapping("/export-pdf")
    @Operation(summary = "Export graph as PDF file", 
              description = "Export a graph in PDF format and download it as a file")
    public ResponseEntity<Resource> exportGraphPdf(
            @Parameter(description = "The graph's id") @RequestParam Long id,
            @Parameter(description = "The commit's id") @RequestParam Long commitId,
            @Parameter( required = false, description = "Flatten the graph") @RequestParam(required = false) Boolean flatten) 
            throws MissingEntityException, IOException   {
        
        String dotContent = graphService.exportGraphToDot(id, commitId, flatten);
        File pdfFile = graphService.convertDotToPdf(dotContent);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=graph-" + id + "-" + commitId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(Files.readAllBytes(pdfFile.toPath())));
    }

    @GetMapping("/export-json")
    @Operation(summary = "Export graph as JSON file", 
              description = "Export a graph in JSON format and download it as a file")
    public ResponseEntity<Resource> exportGraphJson(
            @Parameter(description = "The graph's id") @RequestParam Long id,
            @Parameter(description = "The commit's id") @RequestParam Long commitId,
            @Parameter( required = false, description = "Flatten the graph") @RequestParam(required = false) Boolean flatten) 
            throws MissingEntityException, IOException {
                
                String jsonFile = graphService.exportCommitToJsonFile(id, commitId, flatten);

                     // Create a ByteArrayResource from the DOT content
                ByteArrayResource resource = new ByteArrayResource(jsonFile.getBytes());

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=graph-" + id + "-" + commitId +".json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentLength(resource.contentLength())
                        .body(resource);
    }   



    @Operation(summary = "Check if the graph is consistent", 
    description = "Check if the graph is consistent.")
@RequestMapping(value = "/check-graph-consistency", method = RequestMethod.GET)
public Response<Map<String, Object>> checkGraphConsistency(
@RequestParam @Parameter(description = "The graph's id") Long graphId,
@RequestParam @Parameter(description = "The commit's id") Long commitId
) throws MissingEntityException {
Map<String, Object> result = graphService.checkGraphConsistency(graphId, commitId);
return Response.success("check graph consistency report.", result);
}

}
