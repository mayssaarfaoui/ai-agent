package com.aimanager.agent.dto;

import com.aimanager.agent.models.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NodeDto {
    private final Long id;
    private final String type;
    private final GraphEntityDto graph;
    private final List<NodeDto> parents;
    private final List<NodeDto> connections;

    public NodeDto(GraphNode node, boolean full) {
        this.id = node.getId();
        this.type = node.getType().getName();
        this.graph = GraphEntityDto.of(node.getGraph());
        if (full) {
            this.parents = node.getParents().stream()
                    .map(n -> NodeDto.off(n))
                    .collect(Collectors.toList());
            this.connections = node.getConnections().stream()
                    .map(n -> NodeDto.off(n))
                    .collect(Collectors.toList());
        } else {
            this.parents = new ArrayList<>();
            this.connections = new ArrayList<>();
        }
    }

    public static NodeDto of(GraphNode node) {
        switch (node.getType()) {
            case QUESTION:
                return QuestionNodeDto.of((QuestionNode) node);
            case ANSWER:
                return AnswerNodeDto.of((AnswerNode) node);
            case START:
                return StartNodeDto.of((StartNode) node);
            case END:
                return EndNodeDto.of((EndNode) node);
            case FETCH_DATA:
                return FetchDataNodeDto.of((FetchDataNode) node);
            case ITERATOR:
                return IteratorNodeDto.of((IteratorNode) node);
            case DOWNSTREAM:
                return DownStreamDto.of((DownStreamNode) node);
            case STATEMENT:
                return StatementNodeDto.of((StatementNode) node);
            case LLM:
                return LLMNodeDto.of((LLMNode) node);
            case GOOGLE_TAXONOMY:
                return GoogleTaxonomyNodeDto.of((GoogleTaxonomyNode) node);
            case SUBGRAPH:
                return SubGraphNodeDto.of((SubGraphNode) node);
            case SIMILARITY_SEARCH:
                return SimilaritySearchNodeDto.of((SimilaritySearchNode) node);
            case CREATE_CONVERSATION:
                return CreateConversationDto.of((CreateConversationNode) node);
            case F_F_QUESTION:
                return QuestionWithFreeFormNodeDto.of((QuestionWithFreeFormNode) node);
            case NOTIFICATION:
                return NotificationNodeDto.of((NotificationNode) node);
            default:
                throw new IllegalArgumentException("Invalid node type: " + node.getType());
        }
    }


    public static NodeDto off(GraphNode node) {
        switch (node.getType()) {
            case QUESTION:
                return QuestionNodeDto.off((QuestionNode) node);
            case ANSWER:
                return AnswerNodeDto.off((AnswerNode) node);
            case START:
                return StartNodeDto.off((StartNode) node);
            case END:
                return EndNodeDto.off((EndNode) node);
            case FETCH_DATA:
                return FetchDataNodeDto.off((FetchDataNode) node);
            case ITERATOR:
                return IteratorNodeDto.off((IteratorNode) node);
            case DOWNSTREAM:
                return DownStreamDto.off((DownStreamNode) node);
            case STATEMENT:
                return StatementNodeDto.off((StatementNode) node);
            case LLM:
                return LLMNodeDto.off((LLMNode) node);
            case GOOGLE_TAXONOMY:
                return GoogleTaxonomyNodeDto.off((GoogleTaxonomyNode) node);
            case SUBGRAPH:
                return SubGraphNodeDto.off((SubGraphNode) node);
            case SIMILARITY_SEARCH:
                return SimilaritySearchNodeDto.off((SimilaritySearchNode) node);
            case CREATE_CONVERSATION:
                return CreateConversationDto.off((CreateConversationNode) node);
            case F_F_QUESTION:
                return QuestionWithFreeFormNodeDto.of((QuestionWithFreeFormNode) node);
            case NOTIFICATION:
                return NotificationNodeDto.off((NotificationNode) node);
            default:
                throw new IllegalArgumentException("Invalid node type: " + node.getType());
        }
    }
}
