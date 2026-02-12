package com.aimanager.agent.batch;

import com.aimanager.agent.config.ConnectedUsers;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.Message;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.services.ConversationService;
import com.aimanager.agent.services.NotificationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class ScheduledJob<N extends GraphNode,C extends Conversation,M extends Message> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ScheduledJob.class);

    @Autowired
    ConversationService<N,C,M> conversationService;

    @Autowired
    NotificationService<C> notificationService;

    @Scheduled(cron = "0 */3 * * * *")
    public void performTask() {
        log.info("Job is running at: " + java.time.LocalDateTime.now());
        // Add your job logic here
        List<String> connectedUsers = ConnectedUsers.getInstance().getConnectedUsers();
        connectedUsers.forEach(this::notifyUser);
        log.info("Job finished at: " + java.time.LocalDateTime.now());
    }


    public void notifyUser(C conversation) {
        if (conversation.isStarted()) {
            if (conversation.isStarted30MinutesAgo())
                notificationService.askUserToStartConversation(conversation);

        } else
            notificationService.askUserToStartConversation(conversation);

    }
    public void notifyUser(String userId) {
        C conversation = conversationService.getOldestActiveConversation(Long.parseLong(userId));
        if (conversation != null)
            notifyUser(conversation);
    }
}
