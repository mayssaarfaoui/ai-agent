package com.aimanager.agent.dto;

import com.aimanager.agent.models.GoogleTaxonomyNode;

import lombok.Getter;

@Getter
public class GoogleTaxonomyNodeDto extends NodeDto {

    public GoogleTaxonomyNodeDto(GoogleTaxonomyNode node, boolean full) {
        super(node, full);
    }

    public static GoogleTaxonomyNodeDto of(GoogleTaxonomyNode node) {
        return node == null ? null : new GoogleTaxonomyNodeDto(node, true);
    }

    public static GoogleTaxonomyNodeDto off(GoogleTaxonomyNode node) {
        return node == null ? null : new GoogleTaxonomyNodeDto(node, false);
    }
}
