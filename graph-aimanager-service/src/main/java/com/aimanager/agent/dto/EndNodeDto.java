package com.aimanager.agent.dto;

import com.aimanager.agent.models.EndNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndNodeDto extends NodeDto {

    public EndNodeDto(EndNode node, boolean full) {
        super(node, full);
    }

    public static EndNodeDto of(EndNode node) {
        return node == null ? null : new EndNodeDto(node, false);    
    }

    public static EndNodeDto off(EndNode node) {
        return node == null ? null : new EndNodeDto(node, true);
    }
}
