package com.aimanager.agent.dto;

import com.aimanager.agent.models.SimilaritySearchNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimilaritySearchNodeDto extends NodeDto{

    private String searchQuery;

    public SimilaritySearchNodeDto(SimilaritySearchNode node, boolean full) {
        super(node, full);
        this.searchQuery = node.getSearchQuery();
    }

    public static SimilaritySearchNodeDto of(SimilaritySearchNode node) {
        return node == null ? null : new SimilaritySearchNodeDto(node, false);
    }

    public static SimilaritySearchNodeDto off(SimilaritySearchNode node) {
        return node == null ? null : new SimilaritySearchNodeDto(node, true);
    }
}
