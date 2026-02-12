package com.aimanager.agent.models;

import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.services.StartConversationService;
import com.aimanager.agent.utils.ContextBeanProvider;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@DiscriminatorValue("CREATE_CONVERSATION")
public class CreateConversationNode extends GraphNode{

    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;

    @Column(name="conversation_graph_id")
    private Long conversationGraphId;

    @Column(name="conversation_commit_id")
    private Long conversationCommitId;

    public CreateConversationNode() {
        super(NodeType.CREATE_CONVERSATION);
    }

    @Override
    public CreateConversationNode clone() {
        CreateConversationNode clone = new CreateConversationNode();
        super.copyData(clone);
        clone.setConversationType(this.conversationType);
        clone.setConversationGraphId(this.conversationGraphId);
        clone.setConversationCommitId(this.conversationCommitId);
        return clone;
    }

    @Override
    public String getLabel() {
        return super.getLabel();
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Processing CreateConversationNode with ID: {}", getId());
        Object item = context.getData("item");
        if(item == null) {
            throw new IllegalArgumentException("item is required in context");
        }
        StartConversationService service = ContextBeanProvider.getBean(StartConversationService.class);
        switch (conversationType) {
            case TASK_CONVERSATION: service.createTaskConversation(context, conversationGraphId,conversationCommitId);
            break;
            case PR_CONVERSATION: service.createPrConversation(context, conversationGraphId,conversationCommitId);
            break;
            default :
                throw new IllegalArgumentException("Unsupported conversation type: " + conversationType);
        }
    }
}
