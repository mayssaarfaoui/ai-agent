package com.aimanager.agent.dto;

import com.aimanager.agent.models.StartNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartNodeDto extends NodeDto {

    public StartNodeDto(StartNode node, boolean full) {
        super(node, full);
    }

    public static StartNodeDto of(StartNode node) {
        return node == null ? null : new StartNodeDto(node, false);
    }

    public static StartNodeDto off(StartNode node) {
        return node == null ? null : new StartNodeDto(node, true);
    }

}
