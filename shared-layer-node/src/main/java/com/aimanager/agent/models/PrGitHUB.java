package com.aimanager.agent.models;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PrGitHUB extends Fetchable{
    String id;
    String prTitle;
    String prUrl;
    String prStatus;
    Long creatorId;
    List<Long> reviewers;
    List<Long> reviewerIds;

    @Override
    public String getKey() {
        return id;
    }

    @Override
    public Map<String, String> convertToParameters() {
        Map<String, String> params = new HashMap<>();
        //params.put("organization_id",organizationId.toString());
        return params;
    }

    @Override
    public String toString() {
        return "PrGitHUB{" +
                "id='" + id + '\'' +
                ", prTitle='" + prTitle + '\'' +
                ", prUrl='" + prUrl + '\'' +
                ", prStatus='" + prStatus + '\'' +
                ", reviewers=" + reviewers +
                ", reviewerIds=" + reviewerIds +
                '}';
    }

    public boolean isReviewersNotAssigned() {
        return (reviewers == null || reviewers.isEmpty());
    }

    public List<Long> reviewersNotDone() {
        return reviewerIds.stream()
                .filter(reviewerId -> !reviewers.contains(reviewerId))
                .collect(Collectors.toList());
    }
}
