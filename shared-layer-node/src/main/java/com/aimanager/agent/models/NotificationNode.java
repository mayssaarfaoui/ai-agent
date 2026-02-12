package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.services.OpenPRService;
import com.aimanager.agent.services.notifications.SendNotificationService;
import com.aimanager.agent.utils.ContextBeanProvider;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("NOTIFICATION")
public class NotificationNode extends GraphNode {

    public NotificationNode(Long nodeId, String statement) {
        super(NodeType.NOTIFICATION, nodeId);
    }

    public NotificationNode(String statement) {
        super(NodeType.NOTIFICATION);
    }

    public NotificationNode() {
        super(NodeType.NOTIFICATION);
    }

    @Override
    public NotificationNode clone() {
        NotificationNode clone = new NotificationNode();
        super.copyData(clone);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        String notificationType = (String) context.getData("notificationType");
        if(notificationType == null || notificationType.isEmpty()) {
            throw new IllegalArgumentException("notificationType is required in context");
        }
        String itemId = (String) context.getData("itemId");
        if(itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("itemId is required in context");
        }
        Object item = context.getData("item");
        if(item == null) {
            throw new IllegalArgumentException("item is required in context");
        }
        List<Long> receiversIds = (List<Long>) context.getData("receiversIds");
        if(receiversIds == null || receiversIds.isEmpty()) {
            throw new IllegalArgumentException("receiversIds is required in context");
        }
        SendNotificationService service = ContextBeanProvider.getBean(SendNotificationService.class);
        service.sendNotification(notificationType, itemId, item, receiversIds);
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id;
    }
}
