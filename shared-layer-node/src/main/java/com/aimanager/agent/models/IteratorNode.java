package com.aimanager.agent.models;

import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.resolver.IteratorResolver;
import com.aimanager.agent.services.GraphLoaderService;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
@DiscriminatorValue("ITERATOR")
@Getter
@Setter
public class IteratorNode extends GraphNode {

    @Enumerated(EnumType.STRING)
    private IteratorType iteratorType;

    public IteratorNode(IteratorType iteratorType) {
        super(NodeType.ITERATOR);
        this.iteratorType = iteratorType;
    }

    public IteratorNode() {
        super(NodeType.ITERATOR);
    }

    @Override
    public IteratorNode clone() {
        IteratorNode clone = new IteratorNode();
        super.copyData(clone);
        clone.setIteratorType(this.iteratorType);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Traversing Iterator Node with ID : {} and type : {}.",id,iteratorType);
        //resolve the correct DownStreamAgent based on iterator type
        DownStreamAgent downStreamAgent = IteratorResolver.resolve(iteratorType);
        // Load data from the agent memory node
        downStreamAgent.execute(context);

        Long graphId = context.get("graphId", Long.class);
        Long commitId = context.get("commitId", Long.class);
        Commit commit = GraphLoaderService.getGraph(graphId, commitId);
        if (commit == null) {
            throw new IllegalArgumentException("Commit not found for graphId: "+graphId+" and commitId: "+ commitId);
        }
        GraphNode nextNode = commit.getNodeConnections(this).get(0);
        //process All fetched data
        downStreamAgent.processAll(nextNode,context);
        //clean up stored data
        downStreamAgent.deleteStoredData(context);
    }

    @Override
    public String getLabel() {
        return type.getName() + "_" + id + "_" + iteratorType.getName();
    }
}