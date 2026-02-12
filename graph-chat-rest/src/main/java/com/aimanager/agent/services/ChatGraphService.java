package com.aimanager.agent.services;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.graph.CheckGraph;
import com.aimanager.agent.graph.Visualization;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.request.UploadGraphForm;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatGraphService<GN extends GraphNode> {

    private static final Logger logger = LoggerFactory.getLogger(ChatGraphService.class);


    @Value("${aimanager.graphs.path}")
    private String aigraphsDirectory;

    @Autowired
    CheckGraph checkGraph;

    public boolean hasSubGraphNodes(Commit commit) {
        return commit.getNodes().stream().anyMatch(node -> node.getType() == NodeType.SUBGRAPH);
    }


    public void checkIfGraphContainsSubGraph(Commit commit) {
       if(hasSubGraphNodes(commit)) {
        throw new RuntimeException("Graph contains sub graphs. You should flatten the graph before uploading it.");
       }
    }
    
    
    public void uploadConversationGraph(UploadGraphForm form) {

        // 1. Check file extension
        String originalFilename = form.getFile().getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".json")) {
            throw new RuntimeException("File must be a JSON file.");
        }

        // 2. Validate JSON as Commit object
        ObjectMapper mapper = new ObjectMapper();
        Commit commit;
        try {
            String json = new String(form.getFile().getBytes());
            commit = mapper.readValue(json, Commit.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid JSON or not a valid Commit object.");
        }

        checkIfGraphContainsSubGraph(commit);

        if(!checkGraph.isConsistent(commit)) {
            throw new RuntimeException("Graph is not consistent.");
        }

        File directory = new File(aigraphsDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // create the directory if it doesn't exist
        }

        // Build the filename safely
        String fileName = "graph-" + form.getGraphId() + "-" + form.getCommitId() + ".json";

        // Create the target file
        File targetFile = new File(directory, fileName);

        try {
            // Transfer the uploaded file to the target file
            form.getFile().transferTo(targetFile);
            GraphLoaderService.loadGraph(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

        /*
     * Export the graph to DOT format
     * 
     * @param id the id of the graph
     * @return the DOT content
     * @throws MissingEntityException if the graph or the start node is not found
     */
    public String exportGraphToDot(Long id, Long commitId) throws MissingEntityException {
        logger.info("Exporting graph with id: {}", id);
        Commit commit = GraphLoaderService.getGraph(id, commitId);
        StartNode startNode = GraphLoaderService.getStartNode(commit);
        Visualization<GN> visualization = new Visualization(startNode, commit);
        String dotContent = visualization.drawGraph();
        return dotContent;
    }

}
