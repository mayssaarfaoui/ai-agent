package com.aimanager.agent.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.aimanager.agent.nodes.NodeContext;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("LLM")
@Getter
@Setter
public class LLMNode extends GraphNode {

    private String prompt;

    public LLMNode(String prompt) {
        super(NodeType.LLM);
        this.prompt = prompt;
    }

    public LLMNode() {
        super(NodeType.LLM);
    }

    @Override
    public LLMNode clone() {
        LLMNode clone = new LLMNode();
        super.copyData(clone);
        clone.setPrompt(this.prompt);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        System.out.println("Processing LLM Node with prompt: " + prompt);
        // Simulating LLM processing
        System.out.println("LLM Node processed: Generated response for prompt: " + prompt);
        List<GraphNode> connections = getConnections();
        if (connections != null && !connections.isEmpty()) {
            connections.get(0).process(context); // Continue traversal
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+prompt;
    }
}
