package com.aimanager.agent.controller;
/*
import com.aimanager.agent.models.GraphNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aimanager.agent.Form.traverse.TraverseGraphForm;
import com.aimanager.agent.Form.traverse.VisitGraphForm;
import com.aimanager.agent.dto.VisitorResponseDto;
import com.aimanager.agent.exceptions.MessageException;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.services.GraphVisitorService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/graph-visitor")
public class GraphVisitor<GN extends GraphNode> {

    @Autowired
    private GraphVisitorService<GN> graphVisitorService;

    @GetMapping("/start-visit")
    @Operation(summary = "Visit a graph", description = "Visit a graph")
    public VisitorResponseDto visitGraph(@ModelAttribute VisitGraphForm form) throws MissingEntityException, MessageException {
        GN node = graphVisitorService.visitGraph(form);
        return VisitorResponseDto.of(node);
    }

    @GetMapping("/traverse")
    @Operation(summary = "Traverse a graph", description = "Traverse a graph")
    public VisitorResponseDto traverseGraph(@ModelAttribute TraverseGraphForm form) throws MissingEntityException, MessageException {
        GN node = graphVisitorService.traverseGraph(form);
        return VisitorResponseDto.of(node);
    }

}*/
