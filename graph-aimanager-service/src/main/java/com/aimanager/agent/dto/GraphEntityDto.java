package com.aimanager.agent.dto;

import com.aimanager.agent.models.GraphEntity;
import lombok.Getter;

@Getter
public class GraphEntityDto {
    private final Long id;
    private final String name;
    private final String description;

    public GraphEntityDto(GraphEntity graphEntity) {
        this.id = graphEntity.getId();
        this.name = graphEntity.getName();
        this.description = graphEntity.getDescription();
    }

    public static GraphEntityDto of(GraphEntity graphEntity) {
        return graphEntity == null ? null : new GraphEntityDto(graphEntity);
    }
}
