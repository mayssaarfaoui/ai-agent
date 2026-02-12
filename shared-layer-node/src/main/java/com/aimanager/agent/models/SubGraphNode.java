package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.DiscriminatorValue("SUBGRAPH")
@Getter
@Setter
public class SubGraphNode extends GraphNode{

    @Column(name = "subgraph_id")
    private Long subGraphId;

    @Column(name = "sub_graph_name")
    private String subGraphName;

    @Column(name = "sub_graph_description",columnDefinition = "TEXT")
    private String subGraphDescription;

    @Column(name = "subgraph_commit_id")
    private Long commitId;

    public SubGraphNode() {
        super(NodeType.SUBGRAPH);
    }

    @Override
    public SubGraphNode clone() {
        SubGraphNode clone = new SubGraphNode();
        super.copyData(clone);
        clone.setSubGraphId(this.subGraphId);
        clone.setCommitId(this.commitId);
        clone.setSubGraphName(this.subGraphName);
        clone.setSubGraphDescription(this.subGraphDescription);
        return clone;
    }

    @Override
    public void process(NodeContext nodeContext) {
        // Implement the logic for processing the subgraph node
        logger.info("Processing subgraph node with ID: " + getId());
        // Additional processing logic can be added here
    }

    @Override
    public String getLabel() {
        return type.getName() + "_" + id;
    }
}
