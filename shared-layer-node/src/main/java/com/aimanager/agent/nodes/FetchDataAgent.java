package com.aimanager.agent.nodes;

import com.aimanager.agent.form.ServiceRequestForm;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.FetchableType;
import com.aimanager.agent.models.FetchedResponseType;

public abstract class FetchDataAgent {

    //The node Id
    private Long id;
    //the node type :
    private NodeType type;

    public FetchDataAgent(){
        this.type =  NodeType.FetchDataNode;
    }

    public Long getNodeKey() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeInfo() {
        return "Node : ID :" + id + " - type : " + type;
    }

    public abstract void execute(NodeContext context, FetchableType fetchableType, FetchedResponseType fetchedResponseType, ServiceRequestForm form);

    public abstract NodeContext buildNextNodeContext(Fetchable f);

    public abstract void fetchData(NodeContext context, FetchableType fetchableType, FetchedResponseType fetchedResponseType, ServiceRequestForm form);
}
