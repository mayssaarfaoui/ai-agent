package com.aimanager.agent.graph;

import com.aimanager.agent.ai_manager.Task;
import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.QuestionNode;
import com.aimanager.agent.models.StartNode;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class Visitor<GN extends GraphNode> {
  private Map<String, GN> nodeMap = new HashMap<String, GN>();

  public Visitor() {
  }

  public void setNodeMap(Map<String, GN> nodeMap) {
    this.nodeMap = nodeMap;
  }

  public void addNode(GN node) {
    if (nodeMap.containsKey(node.getId().toString())) {
      System.out.println("Error: Node is already in the graph");
      exit(1);
    }

    nodeMap.put(node.getId().toString(), node);
  }

  /* Visit each node in the graph
   * Print the node
   * Visit the nodes in the branch table
   * */
  public void visit(String nodeId, Task task) {
    if (!nodeMap.containsKey(nodeId)) {
      System.out.println("Error: Node is not in the graph");
      return;
    }
    GN node = nodeMap.get(nodeId);

    if (node == null) {
      System.out.println("Error: Node is null");
      return;
    }
    switch (node.getType()) {
      case START:
        System.out.println("Start node");
        StartNode startNode = (StartNode) node;
        GN nextNode = (GN) startNode.getConnections().get(0);
        if (nextNode == null) {
          System.out.println("Error: Next node is null");
          exit(1);
        }
        visit(nextNode.getId().toString(), task);
        break;
      case END:
        System.out.println("Done processing this task");
        return;
      case QUESTION:
        QuestionNode questionAnswerNode = (QuestionNode) node;

        // Get the system or the user answer from the question
        String question = questionAnswerNode.getQuestionText();
        if (question == null) {
          System.out.println("Error: Question is null");
          exit(1);
        }

            visit(questionAnswerNode.toString(), task);

          break;
      case ANSWER:
          AnswerNode actionNode = (AnswerNode) node;
          if (actionNode == null) {
            System.out.println("Error: Next node is null");
            exit(1);
          }

          GN  nextNodeResponse= (GN) actionNode.getConnections().get(0);

          visit(nextNodeResponse.getId().toString(), task);
          break;
        default:
          System.out.println("Error: Unknown node type");
          exit(1);
    }
  }

  public void visitGraph(StartNode startNode, List<Task> tasks) {
    // fixme move to the AIManager
    // fixme: add a checker to check if all answers are connected to a node and check the graph for errors
    Visitor Visitor = new Visitor();

    int i = 0;
    for (Task t : tasks) {
      System.out.println("Task[" + i + "] " + t.toString());
      visit(startNode.getId().toString(), t);
      i++;
    }
  }

  public Map<String, GN> getNodeMap() {
    return nodeMap;
  }
}

