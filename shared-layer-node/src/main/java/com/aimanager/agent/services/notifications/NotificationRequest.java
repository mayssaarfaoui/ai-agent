package com.aimanager.agent.services.notifications;

import java.util.List;

public class NotificationRequest<T> {

    private String itemId;
    private T item;
    private List<Long> receiversIds;

    public NotificationRequest() {}

    public NotificationRequest(String itemId, T item, List<Long> receiversIds) {
        this.itemId = itemId;
        this.item = item;
        this.receiversIds = receiversIds;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<Long> getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(List<Long> receiversIds) {
        this.receiversIds = receiversIds;
    }
}

