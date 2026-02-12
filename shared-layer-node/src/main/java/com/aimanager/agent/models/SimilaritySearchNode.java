package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("SIMILARITY_SEARCH")
@Getter
@Setter
public class SimilaritySearchNode extends GraphNode {

    private String searchQuery;

    public SimilaritySearchNode(Long nodeId, String searchQuery) {
        super(NodeType.SIMILARITY_SEARCH, nodeId);
        this.searchQuery = searchQuery;
    }

    public SimilaritySearchNode() {
        super(NodeType.SIMILARITY_SEARCH);
    }

    @Override
    public SimilaritySearchNode clone() {
        SimilaritySearchNode clone = new SimilaritySearchNode();
        super.copyData(clone);
        clone.setSearchQuery(this.searchQuery);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        System.out.println("Processing SimilaritySearch Node with query: " + searchQuery);
        // Simulating a similarity search operation
        System.out.println("Similarity search completed: Found relevant results for query: " + searchQuery);
        List<GraphNode> connections = getConnections();
        if (connections != null && !connections.isEmpty()) {
            connections.get(0).process(context); // Continue traversal
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+searchQuery;
    }
}
