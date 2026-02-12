package com.aimanager.agent.graph;

import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.EndNode;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.QuestionNode;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.commits.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CheckGraph {

  /*
   * Check the graph for errors
   * Traverse the graph and check for errors
   * Use a switch statement to check for the type of node
   * If the node is a StartNode, get the next node and check if it is null
   * If the node is a TerminalNode, print a message
   * If the node is a QuestionAnswerNode, get the answer node and check if it is
   * null
   * If the node is a QuestionAnswerNode, get the question and check if it is null
   * If the node is a QuestionAnswerNode, check teh AnswerNode BranchTable for
   * errors
   * such as null values
   */

  public List<Long> visitedNodes = new ArrayList<>();
  public List<String> errorsReport = new ArrayList<>();

  public void checkChildrenSeamticConsistency(GraphNode node, Commit commit, NodeType nodeType) {
    List<GraphNode> children = commit.getNodeConnections(node);
    for (GraphNode child : children) {
      if (child.getType() != nodeType) {
        errorsReport.add("Error: Node with id: " + node.getId() + " has child with id: " + child.getId()
            + " but the child is not of type: " + nodeType);
      }
    }
  }

  public void checkParentsSeamticConsistency(GraphNode node, Commit commit, NodeType nodeType) {
    List<GraphNode> parents = commit.getNodeParents(node);
    for (GraphNode parent : parents) {
      if (parent.getType() != nodeType) {
        errorsReport.add("Error: Node with id: " + node.getId() + " has parent with id: " + parent.getId()
            + " but the parent is not of type: " + nodeType);
      }
    }
  }

  private void checkNodeConsistency(GraphNode node, Commit commit) {
    NodeType nodeType = node.getType();
    switch (nodeType) {
      case START:
        StartNode startNode = (StartNode) node;
        if (!commit.hasChildren(startNode)) {
          errorsReport.add("Error: Start node with id: " + startNode.getId() + " has no children.");
        }
        if (commit.hasParent(startNode)) {
          errorsReport.add("Error: Start node with id: " + startNode.getId() + " has no parents.");
        }
        List<GraphNode> children = commit.getNodeConnections(startNode);
        if (children.size() > 1) {
          errorsReport.add("Error: Start node with id: " + startNode.getId() + " has more than one children.");
        }
        visitedNodes.add(startNode.getId());
        break;

      case END:
        EndNode endNode = (EndNode) node;
        if (commit.hasChildren(endNode)) {
          errorsReport.add("Error: End node with id: " + endNode.getId() + " has children.");
        }
        if (!commit.hasParent(endNode)) {
          errorsReport.add("Error: End node with id: " + endNode.getId() + " has no parents.");
        }
        visitedNodes.add(endNode.getId());
        break;

      case QUESTION:
        QuestionNode questionNode = (QuestionNode) node;
        if (!commit.hasChildren(questionNode)) {
          errorsReport.add("Error: Question node with id: " + questionNode.getId() + " has no children");
        }
        if (!commit.hasParent(questionNode)) {
          errorsReport.add("Error: Question node with id: " + questionNode.getId() + " has no parents");
        }
        checkChildrenSeamticConsistency(questionNode, commit, NodeType.ANSWER);
        visitedNodes.add(questionNode.getId());
        break;

      case ANSWER:
        AnswerNode answerNode = (AnswerNode) node;
        if (!commit.hasChildren(answerNode)) {
          errorsReport.add("Error: Answer node with id: " + answerNode.getId() + " has no children.");
        }
        if (!commit.hasParent(answerNode)) {
          errorsReport.add("Error: Answer node with id: " + answerNode.getId() + " has no parents.");
        }
        checkParentsSeamticConsistency(answerNode, commit, NodeType.QUESTION);
        visitedNodes.add(answerNode.getId());
        break;

      default:
        if (!commit.hasChildren(node)) {
          errorsReport.add("Error: Node with id: " + node.getId() + " has no children.");
        }
        if (!commit.hasParent(node)) {
          errorsReport.add("Error: Node with id: " + node.getId() + " has no parents.");
        }
        visitedNodes.add(node.getId());
        break;
    }
  }

  public Map<String, Object> checkGraph(Commit commit) {
    errorsReport.clear();
    Map<String, Object> errors = new HashMap<>();
    Set<GraphNode> nodes = commit.getNodes();
    for (GraphNode node : nodes) {
      checkNodeConsistency(node, commit);
    }
    if (errorsReport.size() > 0) {
      errors.put("isConsistent", false);
      errors.put("errors", errorsReport);
    } else {
      errors.put("isConsistent", true);
      errors.put("errors", errorsReport);
    }
    return errors;
  }

  public boolean isConsistent(Commit commit) {
    errorsReport.clear();
    Set<GraphNode> nodes = commit.getNodes();
    for (GraphNode node : nodes) {
      checkNodeConsistency(node, commit);
    }
    return errorsReport.size() == 0;
  }
}
