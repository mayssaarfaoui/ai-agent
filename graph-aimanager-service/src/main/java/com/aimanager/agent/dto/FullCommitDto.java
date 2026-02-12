package com.aimanager.agent.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.aimanager.agent.models.commits.Commit;           

import lombok.Getter;

@Getter
public class FullCommitDto extends CommitDto {

    private List<NodeDto> nodes;

    private List<GraphEdgeDto> edges;

    public FullCommitDto(Commit commit) {
        super(commit);
        this.nodes = commit.getNodes().stream().map(NodeDto::of).collect(Collectors.toList());
        this.edges = commit.getEdges().stream().map(GraphEdgeDto::of).collect(Collectors.toList());
    }

    public static FullCommitDto of(Commit commit) {
        return commit == null ? null : new FullCommitDto(commit);
    }

}