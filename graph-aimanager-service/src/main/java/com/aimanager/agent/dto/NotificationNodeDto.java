package com.aimanager.agent.dto;

import com.aimanager.agent.models.NotificationNode;
import lombok.Getter;

@Getter
public class NotificationNodeDto extends NodeDto {

    public NotificationNodeDto(NotificationNode node, boolean full) {
        super(node, full);
    }

    public static NotificationNodeDto of(NotificationNode node) {
        return node == null ? null : new NotificationNodeDto(node,false);
    }

    public static NotificationNodeDto off(NotificationNode node) {
        return node == null ? null : new NotificationNodeDto(node,true);
    }

}
