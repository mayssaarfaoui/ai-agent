package com.aimanager.agent.models;


import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import com.aimanager.agent.request.FetchedDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Task extends Fetchable {
    public UUID id;
    public String title;
    public String description;
    public Double duration;
    private TaskStatus status;
    private ScheduleStatus scheduleStatus;
    public String actualEndDate;
    public Long ownerId;
    public Long creatorId;
    public Long requestedById;
    public Long organizationId;
    private String updatedAt;
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", status=" + status +
                ", actualEndDate='" + actualEndDate + '\'' +
                ", ownerId=" + ownerId +
                ", creatorId=" + creatorId +
                ", requestedById=" + requestedById +
                ", organizationId=" + organizationId +
                '}';
    }

    @Override
    public String getKey() {
        return this.id.toString();
    }

    @Override
    public Map<String, String> convertToParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("organization_id",organizationId.toString());
        params.put("owner_id",ownerId.toString());
        params.put("task_id",id.toString());
        return params;
    }
}
