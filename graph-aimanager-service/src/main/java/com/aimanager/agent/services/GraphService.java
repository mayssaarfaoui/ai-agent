package com.aimanager.agent.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aimanager.agent.Form.UpdateGraphForm;
import com.aimanager.agent.repositories.GraphEdgeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateGraphForm;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.graph.CheckGraph;
import com.aimanager.agent.graph.Visualization;
import com.aimanager.agent.models.EndNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.GraphNodeRepository;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.CommitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GraphService<GN extends GraphNode> {

    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    GraphNodeRepository<GN> graphNodeRepository;

    @Autowired
    CheckGraph checkGraph;

    @Autowired
    CommitService commitService;

    @Autowired
    CommitRepository commitRepository;

    @Autowired
    FlattenGraphService flattenGraphService;

    @Autowired
    GraphEdgeRepository graphEdgeRepository;
       /*
     * Creates a start node for the graph if one doesn't exist already.
     * @return The created start node
     */
    public StartNode getStartNode(Commit commit) throws MissingEntityException {
        StartNode startNode = (StartNode) commit.getNodes().stream().filter(node -> node.getType() == NodeType.START).findFirst().orElseThrow(
            () -> new MissingEntityException("Start node of the graph with id : " + commit.getGraph().getId() + " not found"));
        return startNode;
    }

        /*
     * Checks the graph for consistency
     */
    public Map<String, Object> checkGraphConsistency(Long graphId, Long commitId) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        Commit commit = getCommit(commitId, graph);
        Map<String, Object> response = checkGraph.checkGraph(commit);
        return response;
    }

    

    /*
     * Create a start node for the given graph
     * 
     * @param graph the graph to create the start node for
     */
    public GN createStartNode(GraphEntity graph) {
        logger.info("Creating start node for graph with id: {}", graph.getId());
        StartNode startNode = new StartNode();
        startNode.setGraph(graph);
        startNode.setStatus(NodeStatus.ACTIVE);
        return (GN) graphNodeRepository.save( (GN) startNode);
    }

    /*
     * Create an end node for the given graph
     * 
     * @param graph the graph to create the end node for
     */
    public GN createEndNode(GraphEntity graph) {
        logger.info("Creating end node for graph with id: {}", graph.getId());
        EndNode endNode = new EndNode();
        endNode.setGraph(graph);
        endNode.setStatus(NodeStatus.ACTIVE);
        return (GN) graphNodeRepository.save((GN) endNode);
    }

    /*
     * Add a graph
     * 
     * @param form the form to create the graph
     * @return the created graph
     */

    public GraphEntity addGraph(CreateGraphForm form) {
        logger.info("Creating graph with name: {}", form.getName());
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setName(form.getName());
        graphEntity.setDescription(form.getDescription());
        graphEntity = graphRepository.save(graphEntity);
        return graphEntity;
    }


    /*
     * Create a graph
     * 
     * @param form the form to create the graph
     * @return the created graph
     */
    public GraphEntity createGraph(CreateGraphForm form) {
        logger.info("Creating graph with name: {}", form.getName());
        //create graph
        GraphEntity graphEntity = addGraph(form);
        //create commit
        Commit commit = commitService.initiateCommit(graphEntity);
        commit.setLabel("Create graph: "+graphEntity.getName());
        //create start and end nodes
        GN startNode = createStartNode(graphEntity);
        //create end node
        GN endNode = createEndNode(graphEntity);
        //add nodes to commit
        commitService.addToCurrentCommit(commit, startNode);
        commitService.addToCurrentCommit(commit, endNode);
        //save commit
        commitService.saveCommit(commit);
        return graphEntity;
    }

    /*
     * Get a graph by its id
     * 
     * @param id the id of the graph
     * @return the graph
     * @throws MissingEntityException if the graph is not found
     */
    public GraphEntity getGraph(Long id) throws MissingEntityException {
        logger.info("Getting graph with id: {}", id);
        return graphRepository.findById(id).orElseThrow(
                () -> new MissingEntityException("Graph with id : " + id + " not found"));
    }

    /**
     * Delete all graph edges
     * @param graph
     */
    @Transactional
    public void deleteGraphEdges(GraphEntity graph){
        List<GN> nodes = graphNodeRepository.findAllByGraph(graph);
        if(nodes != null && !nodes.isEmpty()){
            List<Long> nodesIds = nodes.stream().map(GN::getId).collect(Collectors.toList());
            graphEdgeRepository.deleteByStartNodeIdInOrTargetNodeIdIn(nodesIds,nodesIds);
        }
    }

    /**
     * Delete the given graph
     * All related commits and nodes will be deleted also
     * @param id
     * @throws MissingEntityException
     */
    @Transactional
    public void deleteGraph(Long id) throws MissingEntityException {
        logger.info("Delete graph with id: {}", id);
        GraphEntity graph =  getGraph(id);
        deleteGraphEdges(graph);
        commitRepository.deleteByGraph(graph);
        graphNodeRepository.deleteByGraph(graph);
        graphRepository.delete(graph);
    }

    /*
     * Get all graphs
     * 
     * @return the list of graphs
     */
    public Page<GraphEntity> getGraphs(Pageable pageable) {
        logger.info("Getting all graphs");
        return graphRepository.findAll(pageable);
    }

    /*
     * Get the start node of the graph
     * 
     * @param graph the graph
     * @return the start node
     * @throws MissingEntityException if the start node is not found
     */
    public StartNode getStartNode(GraphEntity graph) throws MissingEntityException {
        logger.info("Getting start node of the graph with id: {}", graph.getId());
        return (StartNode) graphNodeRepository.findByGraphAndTypeAndStatus(graph, NodeType.START, NodeStatus.ACTIVE).orElseThrow(
                () -> new MissingEntityException("Start node of the graph with id : " + graph.getId() + " not found"));
    }

    public Commit getCommit(Long commitId, GraphEntity graph) throws MissingEntityException {
        return commitRepository.findByIdAndGraph(commitId, graph)
        .orElseThrow(() -> new MissingEntityException("Commit not found with id: " + commitId + " and graph id: " + graph.getId()));
    }


    public Commit flattenGraph(Commit commit) {
        return commit;
    }

    /*
     * Export the graph to DOT format
     * 
     * @param id the id of the graph
     * @return the DOT content
     * @throws MissingEntityException if the graph or the start node is not found
     */
    public String exportGraphToDot(Long id, Long commitId, Boolean flatten) throws MissingEntityException {
        logger.info("Exporting graph with id: {}", id);
        GraphEntity graphEntity = getGraph(id);
        Commit commit = getCommit(commitId, graphEntity);
        if (flatten != null && flatten) {
           commit = flattenGraphService.flattenGraph(commit);
        }
        StartNode startNode = (StartNode) getStartNode(commit);
        Visualization visualization = new Visualization(startNode, commit);
        String dotContent = visualization.drawGraph();
        return dotContent;
    }

    /*
     * Convert DOT content to PDF file and return the generated file
     * 
     * @param dotContent the DOT content to convert
     * @return the generated PDF file
     * @throws IOException if there is an error during the conversion
     */
    public File convertDotToPdf(String dotContent) throws IOException {
        logger.info("Converting DOT content to PDF");
        
        try {
            // Create temporary files for both DOT and PDF
            File tempDotFile = File.createTempFile("graph_", ".dot");
            File tempPdfFile = File.createTempFile("graph_", ".pdf");
            
            // Write DOT content to temporary file
            FileWriter writer = new FileWriter(tempDotFile);
            writer.write(dotContent);
            writer.close();

            // Build the GraphViz command
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("dot", "-Tpdf", tempDotFile.getAbsolutePath(), "-o", tempPdfFile.getAbsolutePath());

            // Execute the command
            Process process = processBuilder.start();
            
            // Wait for the process to complete
            int exitCode = process.waitFor();
            
            // Delete temporary DOT file as we don't need it anymore
            tempDotFile.delete();

            if (exitCode != 0) {
                // Read error stream if the process failed
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorMessage = errorReader.lines().collect(Collectors.joining("\n"));
                tempPdfFile.delete(); // Clean up PDF file in case of error
                throw new IOException("GraphViz conversion failed: " + errorMessage);
            }

            // Configure the PDF file to be deleted on JVM exit
            tempPdfFile.deleteOnExit();
            
            logger.info("DOT content successfully converted to PDF");
            return tempPdfFile;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("DOT to PDF conversion was interrupted", e);
        }
    }

    public String exportCommitToJsonFile(Long graphId, Long commitId, Boolean flatten) throws MissingEntityException {
        GraphEntity graph = getGraph(graphId);
        // Fetch the commit object (implement this as needed)
        Commit commit = getCommit(commitId, graph);
        if (flatten != null && flatten) {
            commit = flattenGraphService.flattenGraph(commit);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonContent = mapper.writeValueAsString(commit);
            return jsonContent;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to export commit to JSON file: "+ e.getMessage());
        }
    }

    public GraphEntity editGraph(UpdateGraphForm form) throws MissingEntityException {
        logger.info("Editing graph with id: {}", form.getId());
        GraphEntity graphEntity = getGraph(form.getId());
        graphEntity.setName(form.getName());
        graphEntity.setDescription(form.getDescription());
        graphEntity = graphRepository.save(graphEntity);
        return graphEntity;
    }



}
