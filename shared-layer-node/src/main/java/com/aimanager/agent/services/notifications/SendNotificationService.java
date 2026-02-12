package com.aimanager.agent.services.notifications;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendNotificationService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SendNotificationService.class);

    public void sendNotification(String notificationType, String itemId,  Object item, List<Long> receiversIds) {
        logger.info("Sending notification of type: {} to receivers: {}", notificationType, receiversIds);
        /*RestTemplate restTemplate = new RestTemplate();
        // Implementation for sending notification
        NotificationHttpClient client =
                new NotificationHttpClient(
                        restTemplate,
                        "https://alpha.teamboost.ai/teamboost-nf/api/notifications/gh-event",
                        "DST:b29c5a24d24ae2c950a32672a6369f04dcb22442"
                );
        client.sendNotification(
                notificationType,
                "NOTIFICATION",
                itemId,
                item,
                receiversIds
        );*/
    }

    public void sendNotification(String notificationType, String itemId, Object item, Long receiver) {
        // Implementation for sending notification
        List<Long> receiversIds = new ArrayList<>();
        receiversIds.add(receiver);
        sendNotification(notificationType, itemId, item, receiversIds);
    }
}
