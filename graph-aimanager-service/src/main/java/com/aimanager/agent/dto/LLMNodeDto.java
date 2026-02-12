package com.aimanager.agent.dto;

import lombok.Getter;

import com.aimanager.agent.models.LLMNode;

@Getter
public class LLMNodeDto extends NodeDto {

    private String prompt;

    public LLMNodeDto(LLMNode node) {
        super(node,false);
        this.prompt = node.getPrompt();
    }

    public static LLMNodeDto of(LLMNode node) {
        return node == null ? null : new LLMNodeDto(node);
    }

    public static LLMNodeDto off(LLMNode node) {
        return node == null ? null : new LLMNodeDto(node);
    }
}