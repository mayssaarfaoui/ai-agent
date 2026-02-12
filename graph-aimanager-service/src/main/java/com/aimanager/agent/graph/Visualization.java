package com.aimanager.agent.graph;

import com.aimanager.agent.models.*;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.exit;

/**
 * The Visualization class will be responsible for drawing the graph
 * using GraphViz and writing it to a file.
 * Visit the nodes in the graph and draw them
 * <p>
 * Command: dot -Tpdf graph.dot > graph.pdf
 *
 */

public class Visualization<GN extends GraphNode> {

    private static final Logger logger = LoggerFactory.getLogger(Visualization.class);

    static Set<GraphNode> visited = new HashSet<>();

    private final StartNode startNode;

    private static Commit defaultCommit;

    public Visualization(StartNode startNode, Commit commit) {
        this.startNode = startNode;
        this.defaultCommit = commit;
        visited.clear();
    }

  /*
  Debug the graph : 
  Draw the nodes that are not connected to the main graph
  */

    public void graphDebug(StringBuilder dot) {
        Set<GraphNode> nodes = defaultCommit.getNodes();
        for (GraphNode node : nodes) {
            if (!visited.contains(node)) {
                addGraphVizNode(dot, node.getId().toString(), getLabel(node), "ellipse", "red");
                defaultCommit.getNodeConnections(node).forEach(connection -> {
                    traverseGraph(node.getId().toString(), connection, dot);
                });
            }
        }
    }

  /*
  Draw the graph using GraphViz
  And write it to a file
  Visit the nodes in the graph and draw them
   */

    public String drawGraph() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        traverseGraph(null, startNode, dot);
        graphDebug(dot);
        dot.append("}\n");
        return dot.toString();
    }

    // check if id contains digits else exit
    private static void checkId(String id) {
        if (!Utils.containsOnlyDigits(id)) {
            System.out.println("Error: ID does not contain digits" + id);
            exit(1);
        }
    }

    private static void addGraphVizNode(StringBuilder dot, String id, String label, String shape, String color) {
        checkId(id);

        //char [] charLabel = label.toCharArray();
        dot.append("  ").append(id).append(" [label=\"").append(label).append("\", shape=" + shape + ", color=" + color + "];\n");
    }


    private static void addGraphVizEdge(StringBuilder dot, String id0, String id1) {
        checkId(id0);
        checkId(id1);

        dot.append("  ").append(id0).append(" -> ").append(id1).append(";\n");
    }

    public boolean isTerminal(GN node) {
        return node.getType() == NodeType.END;
    }

    public static String getLabel(GraphNode node) {
        NodeType type = node.getType();
        switch (type) {
            case START:
                StartNode startNode = (StartNode) node;
                return startNode.getLabel();
            case END:
                EndNode terminalNode = (EndNode) node;
                return terminalNode.getLabel();
            case QUESTION:
                QuestionNode questionAnswerNode = (QuestionNode) node;
                return questionAnswerNode.getLabel();
            case ANSWER:
                AnswerNode answerNode = (AnswerNode) node;
                return answerNode.getLabel();
            case FETCH_DATA:
                FetchDataNode fetchDataNode = (FetchDataNode) node;
                return fetchDataNode.getLabel();
            case ITERATOR:
                IteratorNode iteratorNode = (IteratorNode) node;
                return iteratorNode.getLabel();
            case DOWNSTREAM:
                DownStreamNode downStreamNode = (DownStreamNode) node;
                return downStreamNode.getLabel();
            case GOOGLE_TAXONOMY:
                GoogleTaxonomyNode googleTaxonomyNode = (GoogleTaxonomyNode) node;
                return googleTaxonomyNode.getLabel();
            case STATEMENT:
                StatementNode statementNode = (StatementNode) node;
                return statementNode.getLabel();
            case SUBGRAPH:
                SubGraphNode subGraphNode = (SubGraphNode) node;
                return subGraphNode.getLabel();
            case SIMILARITY_SEARCH:
                SimilaritySearchNode similaritySearchNode = (SimilaritySearchNode) node;
                return similaritySearchNode.getLabel();
            case CREATE_CONVERSATION:
                CreateConversationNode createConversationNode = (CreateConversationNode) node;
                return createConversationNode.getLabel();
            case F_F_QUESTION:
                QuestionWithFreeFormNode question = (QuestionWithFreeFormNode) node;
                return node.getLabel();
            case NOTIFICATION:
                NotificationNode notificationNode = (NotificationNode) node;
                return notificationNode.getLabel();
            default:
                throw new IllegalArgumentException("Unknown node type: " + type);
        }
    }

    public String getId(GN node) {
        NodeType type = node.getType();
        switch (type) {
            case START:
                StartNode startNode = (StartNode) node;
                return startNode.getId().toString();
            case END:
                EndNode terminalNode = (EndNode) node;
                return terminalNode.getId().toString();
            case QUESTION:
                QuestionNode questionAnswerNode = (QuestionNode) node;
                return questionAnswerNode.getId().toString();
            case ANSWER:
                AnswerNode answerNode = (AnswerNode) node;
                return answerNode.getId().toString();
            case FETCH_DATA:
                FetchDataNode fetchDataNode = (FetchDataNode) node;
                return fetchDataNode.getId().toString();
            case ITERATOR:
                IteratorNode iteratorNode = (IteratorNode) node;
                return iteratorNode.getId().toString();
            case DOWNSTREAM:
                DownStreamNode downStreamNode = (DownStreamNode) node;
                return downStreamNode.getId().toString();
            case GOOGLE_TAXONOMY:
                GoogleTaxonomyNode googleTaxonomyNode = (GoogleTaxonomyNode) node;
                return googleTaxonomyNode.getId().toString();
            case STATEMENT:
                StatementNode statementNode = (StatementNode) node;
                return statementNode.getId().toString();
            case SUBGRAPH:
                SubGraphNode subGraphNode = (SubGraphNode) node;
                return subGraphNode.getId().toString();
            case SIMILARITY_SEARCH:
                SimilaritySearchNode similaritySearchNode = (SimilaritySearchNode) node;
                return similaritySearchNode.getId().toString();
            case CREATE_CONVERSATION:
                CreateConversationNode createConversationNode = (CreateConversationNode) node;
                return createConversationNode.getId().toString();
            case F_F_QUESTION:
                QuestionWithFreeFormNode question = (QuestionWithFreeFormNode) node;
                return question.getId().toString();
            case NOTIFICATION:
                NotificationNode notificationNode = (NotificationNode) node;
                return notificationNode.getId().toString();
            default:
                throw new IllegalArgumentException("Unknown node type: " + type);
        }
    }


    public static List<GraphNode> getNodeConnections(Commit commit, GraphNode node) {
        List<GraphEdge> edges = getEdgesByStartNode(commit, node);
        return edges.stream().map(edge -> edge.getTargetNode()).collect(Collectors.toList());
    }

    public static List<GraphEdge> getEdgesByStartNode(Commit commit, GraphNode startNode) {
        return commit.getEdges().stream().filter(edge -> edge.getStartNode().equals(startNode)).collect(Collectors.toList());
    }

    public static void traverseGraph(String parentId, GraphNode node, StringBuilder dot) {
        if (node == null) return;

        if (visited.contains(node)) {
            addGraphVizEdge(dot, parentId, node.getId().toString());
            return;
        } else {
            visited.add(node);
        }

        logger.info("Visualization: Traversing node with Id {} and type: {}", node.getId(), node.getType());

        switch (node.getType()) {
            case START:
                StartNode startNode = (StartNode) node;
                String startNodeId = startNode.getId().toString();
                String startLabel = startNode.getLabel();
                addGraphVizNode(dot, startNodeId, startLabel, "ellipse", "blue");

                //GraphNode nextNode = startNode.getConnections().get(0);
                //if (nextNode == null) {
                //  System.out.println("Error: Next node is null");
                //  exit(1);
                //}

                //traverseGraph(startNodeId, nextNode, dot);
        /*startNode.getConnections().forEach(connection -> {
          traverseGraph(startNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(startNode).forEach(connection -> {
                    traverseGraph(startNodeId, connection, dot);
                });
                break;
            case END:
                EndNode terminalNode = (EndNode) node;
                String terminalNodeId = terminalNode.getId().toString();
                String terminalLabel = terminalNode.getLabel();
                addGraphVizNode(dot, terminalNodeId, terminalLabel, "ellipse", "red");
                addGraphVizEdge(dot, parentId, terminalNodeId);
                return;
            case QUESTION:
                QuestionNode questionAnswerNode = (QuestionNode) node;

                // Get the system or the user answer from the question
                String question = questionAnswerNode.getQuestionText();
                if (question == null) {
                    System.out.println("Error: Question is null");
                    exit(1);
                }

                String questionID = questionAnswerNode.getId().toString();
                String questionLabel = questionAnswerNode.getLabel();
                addGraphVizNode(dot, questionID, questionLabel, "rectangle", "green");
                addGraphVizEdge(dot, parentId, questionID);

                // Now iterate over the branches and create the graph nodes and edges
                // AnswerNode answerNode = questionAnswerNode.getAnswerNode();
                //BranchTable branchTable = answerNode.getBranchTable();

                // go through the branch table of the question answer node
                Map<String, GraphNode> branchTable = questionAnswerNode.getBranchTable();
        
        /*for (String key : branchTable.keySet()) {
          GraphNode branchNode = branchTable.get(key);
          BranchLabelAndId branchLabelAndId = new BranchLabelAndId(branchNode);
          String branchId = branchLabelAndId.getId();
          String branchLabel = branchLabelAndId.getLabel();
          String keyID = branchNode.getId().toString();
          // Make a node for the key/answer
          String branchSelector = "branch_selector_" + key + "_" + branchId;
          addGraphVizNode(dot, keyID, branchSelector, "rectangle", "red");
          addGraphVizEdge(dot, questionID, keyID);
          traverseGraph(keyID, branchNode, dot);
        }*/
        /*questionAnswerNode.getConnections().forEach(connection -> {
          traverseGraph(questionID, connection, dot);
        });*/
                defaultCommit.getNodeConnections(questionAnswerNode).forEach(connection -> {
                    traverseGraph(questionID, connection, dot);
                });

                break;
            case ANSWER:
                AnswerNode answerNode = (AnswerNode) node;
                String answerNodeId = answerNode.getId().toString();
                String actionLabel = answerNode.getLabel();
                addGraphVizNode(dot, answerNodeId, actionLabel, "rectangle", "purple");
                addGraphVizEdge(dot, parentId, answerNodeId);
                //traverseGraph(parentId, answerNode.getConnections().get(0), dot);
                // traverse the graph for the answer node
        /*answerNode.getConnections().forEach(connection -> {
          traverseGraph(answerNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(answerNode).forEach(connection -> {
                    traverseGraph(answerNodeId, connection, dot);
                });
                break;

            case FETCH_DATA:
                FetchDataNode fetchDataNode = (FetchDataNode) node;
                String fetchDataNodeId = fetchDataNode.getId().toString();
                String fetchDataLabel = fetchDataNode.getLabel();
                addGraphVizNode(dot, fetchDataNodeId, fetchDataLabel, "rectangle", "orange");
                addGraphVizEdge(dot, parentId, fetchDataNodeId);
                //traverseGraph(fetchDataNodeId, fetchDataNode.getConnections().get(0), dot);
        /*fetchDataNode.getConnections().forEach(connection -> {
          traverseGraph(fetchDataNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(fetchDataNode).forEach(connection -> {
                    traverseGraph(fetchDataNodeId, connection, dot);
                });
                break;

            case ITERATOR:
                IteratorNode iteratorNode = (IteratorNode) node;
                String iteratorNodeId = iteratorNode.getId().toString();
                String iteratorLabel = iteratorNode.getLabel();
                addGraphVizNode(dot, iteratorNodeId, iteratorLabel, "rectangle", "yellow");
                addGraphVizEdge(dot, parentId, iteratorNodeId);
                //traverseGraph(iteratorNodeId, iteratorNode.getConnections().get(0), dot);
        /*iteratorNode.getConnections().forEach(connection -> {
          traverseGraph(iteratorNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(iteratorNode).forEach(connection -> {
                    traverseGraph(iteratorNodeId, connection, dot);
                });
                break;

            case DOWNSTREAM:
                DownStreamNode downStreamNode = (DownStreamNode) node;
                String downStreamNodeId = downStreamNode.getId().toString();
                String downStreamLabel = downStreamNode.getLabel();
                addGraphVizNode(dot, downStreamNodeId, downStreamLabel, "rectangle", "brown");
                addGraphVizEdge(dot, parentId, downStreamNodeId);
                //traverseGraph(downStreamNodeId, downStreamNode.getConnections().get(0), dot);
        /*downStreamNode.getConnections().forEach(connection -> {
          traverseGraph(downStreamNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(downStreamNode).forEach(connection -> {
                    traverseGraph(downStreamNodeId, connection, dot);
                });
                break;

      /*case ACTION:
        ActionNode actionNode = (ActionNode) node;
        String actionNodeId = actionNode.getId().toString();
        String actionLabel = actionNode.getLabel();
        addGraphVizNode(dot, actionNodeId, actionLabel, "rectangle", "purple");
        addGraphVizEdge(dot, parentId, actionNodeId);
        traverseGraph(parentId, actionNode.getNextNode(), dot);*/
            case GOOGLE_TAXONOMY:
                GoogleTaxonomyNode googleTaxonomyNode = (GoogleTaxonomyNode) node;
                String googleTaxonomyNodeId = googleTaxonomyNode.getId().toString();
                String googleTaxonomyLabel = googleTaxonomyNode.getLabel();
                addGraphVizNode(dot, googleTaxonomyNodeId, googleTaxonomyLabel, "rectangle", "pink");
                addGraphVizEdge(dot, parentId, googleTaxonomyNodeId);
                //traverseGraph(googleTaxonomyNodeId, googleTaxonomyNode.getConnections().get(0), dot);
        /*googleTaxonomyNode.getConnections().forEach(connection -> {
          traverseGraph(googleTaxonomyNodeId, connection, dot);
        });*/
                defaultCommit.getNodeConnections(googleTaxonomyNode).forEach(connection -> {
                    traverseGraph(googleTaxonomyNodeId, connection, dot);
                });
                break;
            case STATEMENT:
                StatementNode statementNode = (StatementNode) node;
                String statementNodeId = statementNode.getId().toString();
                String statementLabel = statementNode.getLabel();
                addGraphVizNode(dot, statementNodeId, statementLabel, "rectangle", "brown");
                addGraphVizEdge(dot, parentId, statementNodeId);
                defaultCommit.getNodeConnections(statementNode).forEach(connection -> {
                    traverseGraph(statementNodeId, connection, dot);
                });
                break;
            case SUBGRAPH:
                SubGraphNode subGraphNode = (SubGraphNode) node;
                String subGraphNodeId = subGraphNode.getId().toString();
                String subGraphLabel = subGraphNode.getLabel();
                addGraphVizNode(dot, subGraphNodeId, subGraphLabel, "rectangle", "brown");
                addGraphVizEdge(dot, parentId, subGraphNodeId);
                defaultCommit.getNodeConnections(subGraphNode).forEach(connection -> {
                    traverseGraph(subGraphNodeId, connection, dot);
                });
                break;
            case SIMILARITY_SEARCH:
                SimilaritySearchNode similaritySearchNode = (SimilaritySearchNode) node;
                String similaritySearchNodeId = similaritySearchNode.getId().toString();
                String similaritySearchLabel = similaritySearchNode.getLabel();
                addGraphVizNode(dot, similaritySearchNodeId, similaritySearchLabel, "rectangle", "cyan");
                addGraphVizEdge(dot, parentId, similaritySearchNodeId);
                defaultCommit.getNodeConnections(similaritySearchNode).forEach(connection -> {
                    traverseGraph(similaritySearchNodeId, connection, dot);
                });
                break;
            case CREATE_CONVERSATION:
                CreateConversationNode createConversationNode = (CreateConversationNode) node;
                String createConversationNodeId = createConversationNode.getId().toString();
                String createConversationLabel = createConversationNode.getLabel();
                addGraphVizNode(dot, createConversationNodeId, createConversationLabel, "rectangle", "magenta");
                addGraphVizEdge(dot, parentId, createConversationNodeId);
                defaultCommit.getNodeConnections(createConversationNode).forEach(connection -> {
                    traverseGraph(createConversationNodeId, connection, dot);
                });
                break;
            case F_F_QUESTION:
                QuestionWithFreeFormNode ffQuestion = (QuestionWithFreeFormNode) node;
                String ffQuestionNodeId = ffQuestion.getId().toString();
                String ffQuestionLabel = ffQuestion.getLabel();
                addGraphVizNode(dot, ffQuestionNodeId, ffQuestionLabel, "rectangle", "green");
                addGraphVizEdge(dot, parentId, ffQuestionNodeId);
                defaultCommit.getNodeConnections(ffQuestion).forEach(connection -> {
                    traverseGraph(ffQuestionNodeId, connection, dot);
                });
                break;
            case NOTIFICATION:
                NotificationNode notificationNode = (NotificationNode) node;
                String notificationNodeId = notificationNode.getId().toString();
                String notificationLabel = notificationNode.getLabel();
                addGraphVizNode(dot, notificationNodeId, notificationLabel, "rectangle", "teal");
                addGraphVizEdge(dot, parentId, notificationNodeId);
                defaultCommit.getNodeConnections(notificationNode).forEach(connection -> {
                    traverseGraph(notificationNodeId, connection, dot);
                });
                break;
            default:
                throw new IllegalArgumentException("Unknown node type: " + node.getType());
        }
    }

    private static class BranchLabelAndId {
        private final GraphNode branchNode;
        private final String branchId;
        private final String branchLabel;

        public BranchLabelAndId(GraphNode branchNode) {
            this.branchNode = branchNode;

            Pair<String, String> data = getData();
            this.branchLabel = data.getKey();
            this.branchId = data.getValue();
        }

        private Pair<String, String> getData() {
            String branchNodeId = null;
            String branchLabel = null;

            // Cast the branch node to the correct type
            NodeType branchNodeType = branchNode.getType();
            switch (branchNodeType) {
                case END:
                    EndNode terminalNode1 = (EndNode) branchNode;
                    branchNodeId = terminalNode1.getId().toString();
                    branchLabel = terminalNode1.getLabel();
                    break;
                case ANSWER:
                    AnswerNode actionNode = (AnswerNode) branchNode;
                    branchNodeId = actionNode.getId().toString();
                    branchLabel = actionNode.getLabel();
                    break;
                case QUESTION:
                    QuestionNode questionAnswerNode1 = (QuestionNode) branchNode;
                    branchNodeId = questionAnswerNode1.getId().toString();
                    branchLabel = questionAnswerNode1.getLabel();
                    break;
                case FETCH_DATA:
                    FetchDataNode fetchDataNode1 = (FetchDataNode) branchNode;
                    branchNodeId = fetchDataNode1.getId().toString();
                    branchLabel = fetchDataNode1.getLabel();
                    break;
                case ITERATOR:
                    IteratorNode iteratorNode1 = (IteratorNode) branchNode;
                    branchNodeId = iteratorNode1.getId().toString();
                    branchLabel = iteratorNode1.getLabel();
                    break;
                case DOWNSTREAM:
                    DownStreamNode downStreamNode1 = (DownStreamNode) branchNode;
                    branchNodeId = downStreamNode1.getId().toString();
                    branchLabel = downStreamNode1.getLabel();
                    break;
                case GOOGLE_TAXONOMY:
                    GoogleTaxonomyNode googleTaxonomyNode1 = (GoogleTaxonomyNode) branchNode;
                    branchNodeId = googleTaxonomyNode1.getId().toString();
                    branchLabel = googleTaxonomyNode1.getLabel();
                    break;
                case STATEMENT:
                    StatementNode statementNode1 = (StatementNode) branchNode;
                    branchNodeId = statementNode1.getId().toString();
                    branchLabel = statementNode1.getLabel();
                    break;
                case SUBGRAPH:
                    SubGraphNode subGraphNode1 = (SubGraphNode) branchNode;
                    branchNodeId = subGraphNode1.getId().toString();
                    branchLabel = subGraphNode1.getLabel();
                    break;
                case SIMILARITY_SEARCH:
                    SimilaritySearchNode similaritySearchNode1 = (SimilaritySearchNode) branchNode;
                    branchNodeId = similaritySearchNode1.getId().toString();
                    branchLabel = similaritySearchNode1.getLabel();
                    break;
                case CREATE_CONVERSATION:
                    CreateConversationNode createConversationNode1 = (CreateConversationNode) branchNode;
                    branchNodeId = createConversationNode1.getId().toString();
                    branchLabel = createConversationNode1.getLabel();
                    break;
                case F_F_QUESTION:
                    QuestionWithFreeFormNode ffQuestion = (QuestionWithFreeFormNode) branchNode;
                    branchNodeId = ffQuestion.getId().toString();
                    branchLabel = ffQuestion.getLabel();
                    break;
                case NOTIFICATION:
                    NotificationNode notificationNode1 = (NotificationNode) branchNode;
                    branchNodeId = notificationNode1.getId().toString();
                    branchLabel = notificationNode1.getLabel();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown node type: " + branchNodeType);
            }

            return new Pair<>(branchLabel, branchNodeId);
        }

        public String getId() {
            return branchId;
        }

        public String getLabel() {
            return branchLabel;
        }
    }

    /*
    Return the id of the node to use when making the edge in the upper level function
     */
    private static String addGraphVizNode(StringBuilder dot, String key, GraphNode n, String shape, String color) {
        NodeType type = n.getType();

        switch (type) {
            case START:
                addGraphVizNode(dot, n.getId().toString(), getLabel(n), shape, color);
                exit(1);
            case END:
                addGraphVizNode(dot, n.getId().toString(), getLabel(n), shape, color);
                return n.getId().toString();
            case QUESTION:
                QuestionNode questionNode = (QuestionNode) n;
                addGraphVizNode(dot, getLabel(n), questionNode.getId().toString(), shape, color);
                return questionNode.getId().toString();
            case ANSWER:
                AnswerNode answerNode = (AnswerNode) n;
                addGraphVizNode(dot, getLabel(n), answerNode.getId().toString(), shape, color);
                return answerNode.getId().toString();
            case FETCH_DATA:
                FetchDataNode fetchDataNode = (FetchDataNode) n;
                addGraphVizNode(dot, getLabel(n), fetchDataNode.getId().toString(), shape, color);
                return fetchDataNode.getId().toString();
            case ITERATOR:
                IteratorNode iteratorNode = (IteratorNode) n;
                addGraphVizNode(dot, getLabel(n), iteratorNode.getId().toString(), shape, color);
                return iteratorNode.getId().toString();
            case DOWNSTREAM:
                DownStreamNode downStreamNode = (DownStreamNode) n;
                addGraphVizNode(dot, getLabel(n), downStreamNode.getId().toString(), shape, color);
                return downStreamNode.getId().toString();
            case GOOGLE_TAXONOMY:
                GoogleTaxonomyNode googleTaxonomyNode = (GoogleTaxonomyNode) n;
                addGraphVizNode(dot, getLabel(n), googleTaxonomyNode.getId().toString(), shape, color);
                return googleTaxonomyNode.getId().toString();
            case STATEMENT:
                StatementNode statementNode = (StatementNode) n;
                addGraphVizNode(dot, getLabel(n), statementNode.getId().toString(), shape, color);
                return statementNode.getId().toString();
            case SUBGRAPH:
                SubGraphNode subGraphNode = (SubGraphNode) n;
                addGraphVizNode(dot, getLabel(n), subGraphNode.getId().toString(), shape, color);
                return subGraphNode.getId().toString();
            case SIMILARITY_SEARCH:
                SimilaritySearchNode similaritySearchNode = (SimilaritySearchNode) n;
                addGraphVizNode(dot, getLabel(n), similaritySearchNode.getId().toString(), shape, color);
                return similaritySearchNode.getId().toString();
            case CREATE_CONVERSATION:
                CreateConversationNode createConversationNode = (CreateConversationNode) n;
                addGraphVizNode(dot, getLabel(n), createConversationNode.getId().toString(), shape, color);
                return createConversationNode.getId().toString();
            case F_F_QUESTION:
                QuestionWithFreeFormNode ffQuestion = (QuestionWithFreeFormNode) n;
                addGraphVizNode(dot, getLabel(n), ffQuestion.getId().toString(), shape, color);
                return ffQuestion.getId().toString();
            case NOTIFICATION:
                NotificationNode notificationNode = (NotificationNode) n;
                addGraphVizNode(dot, getLabel(n), notificationNode.getId().toString(), shape, color);
                return notificationNode.getId().toString();
            default:
                throw new IllegalArgumentException("Unknown node type: " + type);
        }
    }

}
