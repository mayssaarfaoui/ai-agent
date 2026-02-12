package com.aimanager.agent.dto;

import com.aimanager.agent.models.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VisitorResponseDto {
    private Long nodeId;
    private Long subGraphNodeId;
    private NodeType nodeType;
    private String question;
    private String questionType;
    private String answerType;
    private boolean isAnswered;
    private boolean conversationCompleted;
    private List<OptionDto> options;

    public static VisitorResponseDto of(GraphNode node) {
        switch (node.getType()) {
            case QUESTION:
                node.setup();
                return new VisitorResponseDto((QuestionNode) node);
            case GOOGLE_TAXONOMY:
                node.setup();
                return new VisitorResponseDto((GoogleTaxonomyNode) node);
            case FETCH_DATA:
                return new VisitorResponseDto((FetchDataNode) node);
            case STATEMENT:
                return new VisitorResponseDto((StatementNode) node);
            case F_F_QUESTION:
                return new VisitorResponseDto((QuestionWithFreeFormNode) node);
            case END:
                return new VisitorResponseDto((EndNode) node);
            default:
                throw new IllegalArgumentException("Invalid node type: " + node.getType());
        }
    }

    private VisitorResponseDto(EndNode node) {
        this.nodeId = null;
        this.nodeType = node.getType();
        this.question = null;
        this.questionType = null;
        this.answerType = null;
        this.isAnswered = true; // Statements are considered answered
        this.conversationCompleted=true;
    }

    private VisitorResponseDto(QuestionWithFreeFormNode node) {
        this.nodeId = node.getId();
        this.nodeType = node.getType();
        this.question = node.getQuestionText();
        this.questionType = "QuestionWith Free Form";
        this.answerType = "TEXT";
        this.isAnswered = false;
        this.conversationCompleted=false;
        this.options = null;
    }

    private VisitorResponseDto(StatementNode node) {
        this.nodeId = node.getId();
        this.nodeType = node.getType();
        this.question = node.getStatement();
        this.questionType = QuestionType.SINGLE_CHOICE.toString();
        this.answerType = "TEXT";
        this.isAnswered = true; // Statements are considered answered
        this.conversationCompleted=false;
    }

    private VisitorResponseDto(QuestionNode node) {
        this.nodeId = node.getId();
        this.nodeType = node.getType();
        this.question = node.getQuestionText();
        this.questionType = node.getQuestionType().toString();
        this.answerType = node.getAnswerType().toString();
        this.isAnswered = false;
        this.conversationCompleted=false;
        this.options = node.getOptions().entrySet().stream()
                .map(entry -> new OptionDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private VisitorResponseDto(GoogleTaxonomyNode node) {
        this.nodeId = node.getId();
        this.nodeType = node.getType();
        this.question = "what is your favorite taxonomy?";
        this.questionType = QuestionType.SINGLE_CHOICE.toString();
        this.answerType = "TEXT";
        this.isAnswered = false;
        this.conversationCompleted=false;
        this.options = node.getTaxonomyElements().stream()
                .map(option -> new OptionDto(option.getId().toString(), option.getName()))
                .collect(Collectors.toList());
    }

    private VisitorResponseDto(FetchDataNode node) {
        this.nodeId = node.getId();
        this.nodeType = node.getType();
        // More user-friendly instruction
        this.question = buildParameterQuestion(node);
        // Explicit question/answer types
        this.questionType = QuestionType.MULTIPLE_CHOICE.toString();
        this.answerType = "FILL_PARAMETERS";
        this.isAnswered = false;
        this.conversationCompleted=false;
        // Build parameter options with metadata
        this.options = node.getParameters().stream()
                .map(entry -> new OptionDto(entry))
                .collect(Collectors.toList());
    }


    private String buildParameterQuestion(FetchDataNode node) {
        if (node.getParameters().size() == 1) {
            return "Please provide the following parameter:";
        }
        return "Please fill in the following parameters to continue:";
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }
}
