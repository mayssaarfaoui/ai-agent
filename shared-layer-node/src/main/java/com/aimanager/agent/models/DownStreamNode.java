package com.aimanager.agent.models;

import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.resolver.DownStreamResolver;
import com.aimanager.agent.resolver.IteratorResolver;
import com.aimanager.agent.services.DownStreamProcess;
import com.aimanager.agent.services.GraphLoaderService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@DiscriminatorValue("DOWNSTREAM")
@Getter
@Setter
public class DownStreamNode extends GraphNode {

    @Enumerated(EnumType.STRING)
    private DownStreamType downStreamType;

    @JsonIgnore
    @Transient
    private String messageText;

    @JsonIgnore
    @Transient
    private Map<String, String> options;

    public DownStreamNode(){
        super(NodeType.DOWNSTREAM);
    }


    public DownStreamNode(DownStreamType downStreamType){
        super(NodeType.DOWNSTREAM);
        this.downStreamType = downStreamType;
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Execute DownStreamNode with type : {}" + downStreamType);
        DownStreamProcess agent = DownStreamResolver.resolve(downStreamType);
        // Load data from the agent memory node
        agent.process(context);

        Long graphId = context.get("graphId", Long.class);
        Long commitId = context.get("commitId", Long.class);
        Commit commit = GraphLoaderService.getGraph(graphId, commitId);
        if (commit == null) {
            throw new IllegalArgumentException("Commit not found for graphId: "+graphId+" and commitId: "+ commitId);
        }
        GraphNode nextNode = commit.getNodeConnections(this).get(0);
        agent.process(context);
        // agent.deleteStoredData(getId());
    }

    @Override
    public GraphNode clone() {
        DownStreamNode clone = new DownStreamNode();
        super.copyData(clone);
        clone.setDownStreamType(this.downStreamType);
        return  clone;
    }

    @Override
    public String getLabel() {
        return type.getName() + "_" + id + "_" + downStreamType.getName();
    }
}
