package com.aimanager.agent.dto;

import com.aimanager.agent.models.SubGraphNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubGraphNodeDto extends NodeDto {

    private Long subGraphId;
    private String name;
    private String description;
    private Long commitId;


    public SubGraphNodeDto(SubGraphNode subGraphNode, boolean full) {
        super(subGraphNode, full);
        this.subGraphId = subGraphNode.getSubGraphId();
        this.name = subGraphNode.getSubGraphName();
        this.description = subGraphNode.getSubGraphDescription();
        this.commitId = subGraphNode.getCommitId();
    }
    
    public static SubGraphNodeDto of(SubGraphNode node) {
        return node == null ? null : new SubGraphNodeDto(node, false);
    }

    public static SubGraphNodeDto off(SubGraphNode node) {
        return node == null ? null : new SubGraphNodeDto(node, true);
    }
}
