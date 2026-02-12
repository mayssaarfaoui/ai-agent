package com.aimanager.agent.models;

import java.util.Map;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.persistence.EnumType;

import com.aimanager.agent.nodes.NodeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("QUESTION")
public class QuestionNode extends GraphNode {

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    private String questionText;

    @JsonIgnore
    @Transient
    private Map<String, GraphNode> branchTable;  // Maps answer strings to next nodes
    
    @JsonIgnore
    @Transient
    private Map<String, String> options;

    public QuestionNode() {
        super(NodeType.QUESTION);
    }

    public QuestionNode(QuestionType questionType) {
        super(NodeType.QUESTION);
        this.questionType = questionType;
        this.branchTable = new HashMap<>();
    }
    public QuestionNode(Long nodeId, String questionText, QuestionType questionType) {
        super(NodeType.QUESTION, nodeId);
        this.questionText = questionText;
        this.questionType = questionType;
        this.branchTable = new HashMap<>();
    }

    public QuestionNode(String questionText, QuestionType questionType) {
        super(NodeType.QUESTION);
        this.questionText = questionText;
        this.questionType = questionType;
        this.branchTable = new HashMap<>();
    }

    @Override
    public QuestionNode clone() {
        QuestionNode clone = new QuestionNode();
        super.copyData(clone);
        clone.setQuestionText(this.questionText);
        clone.setQuestionType(this.questionType);
        clone.setAnswerType(this.answerType);
        return clone;
    }

    public String getAnswerKey(AnswerNode answerNode) {
        switch (answerType) {
            case TEXT:
                return ((TextAnswer) answerNode).getAnswerKey().toLowerCase();
            case NUMBER:
                return ((NumberAnswer) answerNode).getAnswerKey().toLowerCase();
            case FILE:
                return ((FileAnswer) answerNode).getAnswerKey().toLowerCase();
            case DATE:
                return ((DateAnswer) answerNode).getAnswerKey().toLowerCase();
            default:
                throw new IllegalArgumentException("Invalid answer type: " + answerType);
        }
    }

    public String getAnswerValue(AnswerNode answerNode) {
        switch (answerType) {
            case TEXT:
                return ((TextAnswer) answerNode).getAnswerValue();
            case NUMBER:
                return ((NumberAnswer) answerNode).getAnswerValue();
            case FILE:
                return ((FileAnswer) answerNode).getAnswerValue();
            case DATE:
                return ((DateAnswer) answerNode).getAnswerValue();
            default:
                throw new IllegalArgumentException("Invalid answer type: " + answerType);
        }
    }

    @Override
    public void setup() {
        if(branchTable == null) {
            branchTable = new HashMap<>();
            options = new HashMap<>();
        }
        for (GraphNode node : getConnections()) {
            if (node instanceof AnswerNode) {         
                AnswerNode answerNode = (AnswerNode) node;
                String key = getAnswerKey(answerNode);
                String answerValue = getAnswerValue(answerNode);
                branchTable.put(key, node);
                options.put(key, answerValue);
            }
        }
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Question: " + questionText);
       /* sendToUI("Available answers: " + branchTable.keySet());

        // Send branch table to UI
        sendToUI("Please select an answer by entering its corresponding node ID:");
        for (Map.Entry<String, GraphNode> entry : branchTable.entrySet()) {
            sendToUI(entry.getKey() + " -> Node ID: " + entry.getValue().getId());
        }

        // Receive response and traverse
        String selectedNodeId = receiveNodeIdFromUI();
        traverse(selectedNodeId);*/
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+questionText;
    }
}
