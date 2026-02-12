package com.aimanager.agent.dto;

import com.aimanager.agent.models.DownStreamNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DownStreamDto extends NodeDto{
    private String message;
    private Map<String,String> options;

    public DownStreamDto(DownStreamNode node, boolean full) {
        super(node, full);
        this.message = node.getMessageText();
        this.options = node.getOptions();
    }

    public static DownStreamDto of(DownStreamNode node) {
        return node == null ? null : new DownStreamDto(node,false);
    }

    public static DownStreamDto off(DownStreamNode node) {
        return node == null ? null : new DownStreamDto(node,true);
    }
}
