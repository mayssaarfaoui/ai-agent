package com.aimanager.agent.models.conversation;

import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;


@Getter
@Setter
@Entity
@DiscriminatorValue("PR_CONVERSATION")
public class PRConversation extends Conversation {
    private String prId;
    private String prTitle;
}
