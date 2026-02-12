package com.aimanager.agent.models;

import javax.persistence.*;

import com.aimanager.agent.form.RequestForm;
import com.aimanager.agent.form.ServiceRequestForm;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.nodes.FetchDataAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.services.FetchDataService;
import com.aimanager.agent.services.GraphLoaderService;
import com.aimanager.agent.utils.ContextBeanProvider;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("FETCH_DATA")
@Getter
@Setter
public class FetchDataNode extends GraphNode {

    @Enumerated(EnumType.STRING)
    private FetchableType fetchableType;

    @Enumerated(EnumType.STRING)
    private FetchedResponseType fetchedResponseType;

    @Column(name = "fetch_service_url")
    private String fetchServiceUrl;

    @ElementCollection
    @CollectionTable(
            schema = "aimanager",
            name = "fetch_node_headers",
            joinColumns = @JoinColumn(name = "fetch_node_id")
    )
    @MapKeyColumn(name = "key")
    @Column(
            name = "value",
            columnDefinition = "TEXT"
    )
    private Map<String, String> headers;


    @ElementCollection
    @CollectionTable( schema = "aimanager",
            name = "fetch_node_parameters", joinColumns = @JoinColumn(name = "fetch_node_id"))
    @Column(name = "parameter")
    private List<String> parameters = new ArrayList<>();

    private boolean validated;

    public FetchDataNode(FetchableType fetchableType) {
        super(NodeType.FETCH_DATA);
        this.fetchableType= fetchableType;
    }

    public FetchDataNode() {
        super(NodeType.FETCH_DATA);
    }

    public void addHeader(String key, String value){
        if(this.headers == null)
            this.headers = new HashMap<>();
        headers.put(key,value);
    }

    public void addParameter(String parameter){
        if(this.parameters == null)
            this.parameters = new ArrayList<>();

        this.parameters.add(parameter);
    }

    @Override
    public FetchDataNode clone() {
        FetchDataNode clone = new FetchDataNode();
        super.copyData(clone);
        clone.setFetchServiceUrl(this.fetchServiceUrl);
        clone.setFetchableType(this.fetchableType);
        clone.setFetchedResponseType(this.fetchedResponseType);
        this.headers.forEach((key,value)->clone.addHeader(key,value));
        parameters.forEach(parameter->clone.addParameter(parameter));
        clone.setValidated(this.validated);
        return clone;
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Traversing Fetch Data Node with ID : {} and type : {}.",id,fetchableType);
        //fetch data
        //FetchDataAgent agent = FetchDataResolver.resolve(fetchableType);
        ServiceRequestForm form = new ServiceRequestForm();
        form.setUrl(this.fetchServiceUrl);
        form.setHeaders(getHeaders());
        RequestForm crf = (RequestForm) context.get("requestform");
        //form.setQueryParams(crf.getQueryParams());
        form.setQueryParams(new HashMap<>(crf.getQueryParams()));
        FetchDataAgent agent = ContextBeanProvider.getBean(FetchDataService.class);
        agent.setId(getId());
        agent.execute(context, fetchableType, fetchedResponseType,form);
        //process next nodes
        Long graphId = context.get("graphId", Long.class);
        Long commitId = context.get("commitId", Long.class);
        Commit commit = GraphLoaderService.getGraph(graphId, commitId);
        if (commit == null) {
           throw new IllegalArgumentException("Commit not found for graphId: "+graphId+" and commitId: "+ commitId);
        }
        List<GraphNode> connections = commit.getNodeConnections(this);
        for(GraphNode n:connections){
            //NodeContext nnc = agent.buildNextNodeContext(null);
            //NodeContext nnc = context;
            NodeContext nnc = new NodeContext();
            nnc.set("graphId", graphId);
            nnc.set("commitId", commitId);
            nnc.set("nodeId",getId());
            n.process(nnc);
        }
    }

    @Override
    public String getLabel() {
        return type.getName()+"_"+id+"_"+fetchableType.getName();
    }
}
