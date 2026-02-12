package com.aimanager.agent.dto;

import com.aimanager.agent.models.IteratorNode;
import com.aimanager.agent.models.IteratorType;

import lombok.Getter;

@Getter
public class IteratorNodeDto extends NodeDto {

    private final IteratorType   iteratorType;

    public IteratorNodeDto(IteratorNode iteratorNode, boolean full) {
        super(iteratorNode, full);
        this.iteratorType = iteratorNode.getIteratorType();
    }

    public static IteratorNodeDto of(IteratorNode node) {
        return node == null ? null : new IteratorNodeDto(node,false);
    }

    public static IteratorNodeDto off(IteratorNode node) {
        return node == null ? null : new IteratorNodeDto(node,true);
    }

}
