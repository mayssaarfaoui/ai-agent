package com.aimanager.agent.services;

import com.aimanager.agent.models.PrGitHUB;
import com.aimanager.agent.models.Task;
import com.aimanager.agent.models.conversation.PRConversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.repository.PRConversationRepository;
import com.aimanager.agent.repository.TaskConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StartConversationService {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StartConversationService.class);

    @Autowired
    TaskConversationRepository taskConversationRepository;

    @Autowired
    PRConversationRepository prConversationRepository;

    // Create conversation for tasks


    public LocalDateTime getLocalDateFromEpoch(String dateTimeString) {
        try {
            // Parse the string to OffsetDateTime
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString);
            // Convert OffsetDateTime to LocalDate
            LocalDateTime localDate = offsetDateTime.toLocalDateTime();
            return localDate;
        } catch (Exception e) {
            logger.error("Error while parsing the date : {}", e.getMessage());
            throw new IllegalArgumentException("Error while parsing the date");
        }
    }

    public boolean checkIfThereIsAnActiveConversation(Task task) {
        LocalDateTime dateTime = getLocalDateFromEpoch(task.getUpdatedAt());
        return taskConversationRepository.existsByUserIdAndTaskIdAndStartedAtGreaterThan(task.getOwnerId(), task.getId(), dateTime);
    }


    public void createConversation(Task task, Long graphId, Long commitId) {
        //CreateConversationNode createConversationNode = convertToCreateConversationNode(nextNode);
        TaskConversation conversation = new TaskConversation();
        conversation.setGraphId(graphId);
        conversation.setCommitId(commitId);
        conversation.setUserId(task.getOwnerId());
        conversation.setTaskId(task.getId());
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setTaskTitle(task.getTitle());
        conversation.setStatus(task.getStatus());
        conversation.setScheduleStatus(task.getScheduleStatus());
        conversation.setStarted(false);
        conversation.setEnded(false);
        conversation.setSkipped(false);
        taskConversationRepository.save(conversation);
    }

    public void createTaskConversation(Object item, Long graphId, Long commitId) {
        Task task = (Task) item;
        boolean exists = checkIfThereIsAnActiveConversation(task);
        if (!exists) {
            createConversation(task, graphId, commitId);
        } else {
            logger.info("There is an active conversation for this task with id : {}", task.getId());
        }
    }

    // create conversation for PRs

    public List<Long> getWhoExpectedToReviewPr(List<Long> reviewerIds, List<Long> reviewers) {

        if (reviewerIds == null || reviewerIds.isEmpty()) {
            return Collections.emptyList();
        }

        if (reviewers == null || reviewers.isEmpty()) {
            // No one reviewed yet → all reviewers are pending
            return new ArrayList<>(reviewerIds);
        }

        Set<Long> reviewedSet = new HashSet<>(reviewers);

        return reviewerIds.stream()
                .filter(id -> !reviewedSet.contains(id))
                .collect(Collectors.toList());
    }

    public void createConversation(PrGitHUB pr, Long reviewerId, Long graphId, Long commitId) {
        logger.info("Create task for user :{} about PR : {}",reviewerId,pr.getId());
        PRConversation conversation = new PRConversation();
        conversation.setGraphId(graphId);
        conversation.setCommitId(commitId);
        conversation.setUserId(reviewerId);
        conversation.setPrId(pr.getId());
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setPrTitle(pr.getPrTitle());
        conversation.setStarted(false);
        conversation.setEnded(false);
        conversation.setSkipped(false);
        prConversationRepository.save(conversation);
    }

    public void createPrConversation(Object item, Long graphId, Long commitId) {
        PrGitHUB pr = (PrGitHUB) item;
        List<Long> expected = getWhoExpectedToReviewPr(pr.getReviewerIds(), pr.getReviewers());
        if (expected == null || expected.isEmpty())
            return;
        expected.forEach(reviewr ->
                createConversation(pr, reviewr, graphId, commitId));
    }
}
