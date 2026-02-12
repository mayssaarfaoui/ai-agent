package com.aimanager.agent.controller;

import com.aimanager.agent.services.GraphNodesServices;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph/process")
public class GraphNodesController {

    @Autowired
    private GraphNodesServices graphNodesServices;

    @PostMapping
    public ResponseEntity<String> fetchData(
        @Parameter(required = true, description = "Organization's ID.") @RequestParam Long organizationId,
        @Parameter(required = true, description = "Graph's ID.") @RequestParam Long graphId) {

        graphNodesServices.processData(graphId, organizationId);

        return ResponseEntity.ok("Data processed successfully");
    }

}

