package com.aimanager.agent.models;

import java.util.HashSet;
import java.util.Set;

// Main class to demonstrate usage
public class Main {
    public static void main(String[] args) {
        // Creating nodes with unique IDs
       /* StartNode start = new StartNode(1L);
        QuestionNode question = new ChoiceBasedQuestion(2L, "What is your favorite color?");
        AnswerNode answer1 = new AnswerNode(3L, "Red");
        AnswerNode answer2 = new AnswerNode(4L, "Blue");
        StatementNode statement = new StatementNode(5L, "You chose a color!");
        EndNode end = new EndNode(6L);

        // Connecting nodes
        start.connect(question);
        question.connect(answer1);
        question.connect(answer2);
        answer1.connect(statement);
        answer2.connect(statement);
        statement.connect(end);

        /*
        // Running setup before processing
        start.setup();
        question.setup();

        // Start processing
        start.process();
        *

                // Phase 1: Setup traversal
                sendToUIGlobal("Setting up the graph...");
                setupTraversal(start);
        
                // Phase 2: Process and traverse until end node is reached
                sendToUIGlobal("Starting traversal...");
                processTraversal(start);
        
                // Cycle check
                if (checkForCycles(start)) {
                    sendToUIGlobal("Cycle detected in the graph!");
                } else {
                    sendToUIGlobal("No cycles detected in the graph.");
                }*/
    }

        // Phase 1: Setup traversal
        private static void setupTraversal(GraphNode startNode) {
            Set<String> visited = new HashSet<>();
            setupRecursive(startNode, visited);
        }
    
        private static void setupRecursive(GraphNode node, Set<String> visited) {
            if (visited.contains(node.getId())) {
                return; // Prevent revisiting nodes
            }
            visited.add(node.getId().toString());
            node.setup(); // Perform setup for the node
            for (GraphNode connectedNode : node.getConnections()) {
                setupRecursive(connectedNode, visited);
            }
        }
    
        // Phase 2: Process and traverse until end node is reached
        private static void processTraversal(GraphNode startNode) {
            Set<String> visited = new HashSet<>();
            processRecursive(startNode, visited);
        }
    
        private static void processRecursive(GraphNode node, Set<String> visited) {
            if (visited.contains(node.getId())) {
                return; // Prevent revisiting nodes (avoid cycles)
            }
            visited.add(node.getId().toString());
            node.process(null); // Process the current node
            for (GraphNode connectedNode : node.getConnections()) {
                processRecursive(connectedNode, visited);
            }
        }
    
        // Cycle detection: Check for cycles in the graph
        private static boolean checkForCycles(GraphNode startNode) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();
            return cycleDetectRecursive(startNode, visited, recursionStack);
        }
    
        private static boolean cycleDetectRecursive(GraphNode node, Set<String> visited, Set<String> recursionStack) {
            if (recursionStack.contains(node.getId())) {
                return true; // Cycle detected
            }
            if (visited.contains(node.getId())) {
                return false; // Already visited node, no cycle here
            }
    
            // Mark the node as visited and add it to the recursion stack
            visited.add(node.getId().toString());
            recursionStack.add(node.getId().toString());
    
            // Recurse for all connected nodes
            for (GraphNode connectedNode : node.getConnections()) {
                if (cycleDetectRecursive(connectedNode, visited, recursionStack)) {
                    return true; // Cycle detected in connected nodes
                }
            }
    
            // Remove the node from the recursion stack after finishing its traversal
            recursionStack.remove(node.getId());
            return false; // No cycle detected
        }
    
        // Global UI message sender (simulating external UI interactions)
        private static void sendToUIGlobal(String message) {
            System.out.println("[SYSTEM] " + message);
        }   
}
