package com.aimanager.agent.models.conversation;

import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import com.aimanager.agent.models.Message;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@DiscriminatorValue("TASK_CONVERSATION")
public class TaskConversation extends Conversation {
    protected UUID taskId;
    private String taskTitle;
    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status;
    @Enumerated(EnumType.ORDINAL)
    private ScheduleStatus scheduleStatus;
}
