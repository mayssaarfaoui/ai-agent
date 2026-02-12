package com.aimanager.agent.dto.conversation;

import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import com.aimanager.agent.models.conversation.TaskConversation;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TaskConversationDto extends ConversationDto {
    private final UUID taskId;
    private  final String taskTitle;
    private final TaskStatus status;
    private final ScheduleStatus scheduleStatus;

    public TaskConversationDto(TaskConversation conversation){
        super(conversation);
        this.taskId = conversation.getTaskId();
        this.taskTitle = conversation.getTaskTitle();
        this.status = conversation.getStatus();
        this.scheduleStatus = conversation.getScheduleStatus();
    }

    public static TaskConversationDto of(TaskConversation conversation) {
        return conversation == null ? null : new TaskConversationDto(conversation);
    }
}
