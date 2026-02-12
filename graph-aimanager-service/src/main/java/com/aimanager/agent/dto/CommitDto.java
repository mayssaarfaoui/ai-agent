package com.aimanager.agent.dto;

import com.aimanager.agent.models.commits.Commit;

import lombok.Getter;

@Getter
public class CommitDto {

    private Long id;

    private String label;

    private Long createdAt;

    public CommitDto(Commit commit) {
        this.id = commit.getId();
        this.label = commit.getLabel();
        this.createdAt = commit.getCreatedAt().getTimeInMillis();
    }

    public static CommitDto of(Commit commit) {
        return commit == null ? null : new CommitDto(commit);
    }

}
