package com.aimanager.agent.models;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.aimanager.agent.nodes.NodeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AnswerNode extends GraphNode {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionNode question;

    public AnswerNode() {
        super(NodeType.ANSWER);
    }

    @Override
    public void process(NodeContext context) {
        sendToUI("Answer: " + getLabel());
        /*for (GraphNode node : connections) {
            node.process();
        }*/
        List<GraphNode> connections = getConnections();
        if (connections != null && !connections.isEmpty()) {
            connections.get(0).process(context); // Continue traversal
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id;
    }

    @Override
    public AnswerNode clone() {
        AnswerNode clone = new AnswerNode();
        super.copyData(clone);
        clone.setQuestion(getQuestion().clone());
        return clone;
    }

    @JsonIgnore
    public AnswerType getAnswerType(){
        return getQuestion().getAnswerType();
    }

    @JsonIgnore
    public String getAnswerText(){
        return null;
    }

    @JsonIgnore
    public String getAnswerKey(){
        return null;
    }

    @JsonIgnore
    public String getAnswerValue(){
        return null;
    }
    
    
}
