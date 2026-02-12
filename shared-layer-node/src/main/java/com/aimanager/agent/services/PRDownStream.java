package com.aimanager.agent.services;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.PrGitHUB;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.repository.PRConversationRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class PRDownStream<T extends PrGitHUB> extends DownStreamAgent<Fetchable> {

    /**
     * Constructor to initialize the Downstream Node.
     *
     */
    public PRDownStream() {
        super();
    }

    public PrGitHUB convertPrData(Object data) {
        JsonObject obj = toJsonObject(data);

        PrGitHUB pr = new PrGitHUB();

        // id
        if (obj.has("id") && !obj.get("id").isJsonNull()) {
            pr.setId(obj.get("id").getAsString());
        }

        // PR title
        if (obj.has("prTitle") && !obj.get("prTitle").isJsonNull()) {
            pr.setPrTitle(obj.get("prTitle").getAsString());
        }

        // PR URL
        if (obj.has("prUrl") && !obj.get("prUrl").isJsonNull()) {
            pr.setPrUrl(obj.get("prUrl").getAsString());
        }

        // PR status
        if (obj.has("prStatus") && !obj.get("prStatus").isJsonNull()) {
            pr.setPrStatus(obj.get("prStatus").getAsString());
        }

        // creatorId
        if (obj.has("creatorId") && !obj.get("creatorId").isJsonNull()) {
            pr.setCreatorId(obj.get("creatorId").getAsLong());
        }

        // reviewers (from "reviews" array → creatorId)
        if (obj.has("reviews") && obj.get("reviews").isJsonArray()) {
            List<Long> reviewers = new ArrayList<>();
            for (JsonElement el : obj.getAsJsonArray("reviews")) {
                if (el.isJsonObject()) {
                    JsonObject reviewObj = el.getAsJsonObject();
                    if (reviewObj.has("creatorId") && !reviewObj.get("creatorId").isJsonNull()) {
                        reviewers.add(reviewObj.get("creatorId").getAsLong());
                    }
                }
            }
            pr.setReviewers(reviewers);
        }

        // reviewerIds (already provided in JSON)
        if (obj.has("reviewerIds") && obj.get("reviewerIds").isJsonArray()) {
            List<Long> reviewerIds = new ArrayList<>();
            for (JsonElement el : obj.getAsJsonArray("reviewerIds")) {
                if (!el.isJsonNull()) {
                    reviewerIds.add(el.getAsLong());
                }
            }
            pr.setReviewerIds(reviewerIds);
        }
        return pr;
    }

    @Override
    public void process(Object data, GraphNode nextNode, NodeContext context) {
        try {
        PrGitHUB pr = convertPrData(data);
        logger.info("Process PR : {}.", pr.toString());
        //processPr(pr, nextNode, context);
        NodeContext nc = buildNextNodeContext(pr);
        nc.set("item", pr);
        nextNode.process(nc);
        }catch (Exception e){
            throw new IllegalArgumentException("Data can to be converted to PR");
        }
    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable data) {
        PrGitHUB item = (PrGitHUB) data;
        NodeContext nc = new NodeContext();
        nc.set("itemId", item.getId());
        if (item.isReviewersNotAssigned()) {
            nc.set("notificationType", "GH_REVIEW_REQUEST");
            List<Long> receivers = new ArrayList<>();
            receivers.add(item.getCreatorId());
            nc.set("receiversIds", receivers);
        } else {
            List<Long> pendingReviewers = item.reviewersNotDone();
            if (pendingReviewers == null  || pendingReviewers.isEmpty()) {
                nc.set("notificationType", "GH_REVIEWER_RESPONSE_REMINDER");
                List<Long> receivers = new ArrayList<>();
                receivers.add(item.getCreatorId());
                nc.set("receiversIds", receivers);
            }
            else {
                nc.set("notificationType", "GH_REVIEW_COMPLETION");
                nc.set("receiversIds", pendingReviewers);
            }
        }
        return nc;
    }
}
