package com.aimanager.agent.services;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.commits.Commit;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GraphLoaderService {

    public static Map<String, Commit> graphs = new HashMap<>();

    static {
        try {
            loadGraphs();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load graphs", e);
        }
    }


    public static void loadGraph(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (file.isFile() && file.getName().endsWith(".json")) {
            String graphId = file.getName().replace(".json", "");
            try {
                Commit commit = mapper.readValue(file, Commit.class);
                if(graphs.containsKey(graphId)) {
                    graphs.remove(graphId);
                }
                graphs.put(graphId, commit);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load graph: "+ file.getName() + " " + e.getMessage());   
            }
        }
    }

    public static void loadGraphs() throws IOException {
        
        // Get the user's home directory
        File aigraphsDir = new File("/app/aigraphs");

        // Check if the directory exists and is a directory
        if (!aigraphsDir.exists() || !aigraphsDir.isDirectory()) {
            System.err.println("Directory does not exist: " + aigraphsDir.getAbsolutePath());
            return;
        }

        // Example: Filter only .json files (remove filter to get all files)
        FilenameFilter jsonFilter = (dir, name) -> name.toLowerCase().endsWith(".json");

        // List files (use aigraphsDir.listFiles() for all files)
        File[] files = aigraphsDir.listFiles(jsonFilter);

        if (files == null) {
            System.err.println("Failed to list files in directory: " + aigraphsDir.getAbsolutePath());
            return;
        }

        System.out.println("Found " + files.length + " files in " + aigraphsDir.getAbsolutePath());

        for (File file : files) {
            loadGraph(file);
        }
    }

    public static Commit getGraph(Long graphId, Long commitId) {
        String graphIdStr = graphId.toString();
        String commitIdStr = commitId.toString();
        return graphs.get("graph-" + graphIdStr + "-" + commitIdStr);
    }

    public static StartNode getStartNode(Long graphId, Long commitId) {
        Commit commit = getGraph(graphId, commitId);
        return getStartNode(commit);
    }

    public static StartNode getStartNode(Commit commit) {
        return commit.getNodes().stream()
            .filter(node -> node.getType().equals(NodeType.START))
            .map(node -> (StartNode) node)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No start node found for graph " + commit.getId()));
    }

    public static GraphNode getNode(Long graphId, Long commitId, Long nodeId) {
        Commit commit = getGraph(graphId, commitId);
        return commit.getNodes().stream()
            .filter(node -> node.getId().equals(nodeId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No node found for graph " + graphId + " and commit " + commitId + " and node " + nodeId));
    }

    public static List<GraphNode> getConnections(Long graphId, Long commitId, GraphNode node) {
        Commit commit = getGraph(graphId, commitId);
        return commit.getNodeConnections(node);
    }

    public static void setup(Long graphId, Long commitId, GraphNode node) {
        Commit commit = getGraph(graphId, commitId);
        List<GraphEdge> incomingEdges = commit.getEdgesByTargetNode(node);
        node.setIncomingEdges(incomingEdges);
        List<GraphEdge> outgoingEdges = commit.getEdgesByStartNode(node);
        node.setOutgoingEdges(outgoingEdges);
    }

    public static boolean checkIfGraphExists(Long graphId, Long commitId) {
        String graphIdStr = graphId.toString();
        String commitIdStr = commitId.toString();
        return graphs.containsKey("graph-" + graphIdStr + "-" + commitIdStr);
    }

    

}
