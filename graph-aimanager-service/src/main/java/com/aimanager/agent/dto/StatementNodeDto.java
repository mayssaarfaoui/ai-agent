package com.aimanager.agent.dto;

import com.aimanager.agent.models.StatementNode;

import lombok.Getter;

@Getter
public class StatementNodeDto extends NodeDto {

    private String statement;

    public StatementNodeDto(StatementNode statementNode, boolean full) {
        super(statementNode, full);
        this.statement = statementNode.getStatement();
    }

    public static StatementNodeDto of(StatementNode statementNode) {
        return statementNode == null ? null : new StatementNodeDto(statementNode,false);
    }

    public static StatementNodeDto off(StatementNode statementNode) {
        return statementNode == null ? null : new StatementNodeDto(statementNode,true);
    }

}
