package com.aimanager.agent.dto;

import com.aimanager.agent.models.FetchDataNode;
import com.aimanager.agent.models.FetchableType;
import com.aimanager.agent.models.FetchedResponseType;
import com.aimanager.agent.models.GraphNode;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class FetchDataNodeDto extends NodeDto {

    private FetchableType fetchableType;
    private FetchedResponseType fetchedResponseType;
    private String fetchServiceUrl;
    private Map<String, String> headers;
    private List<String> parameters;
    private boolean validated;


    public FetchDataNodeDto(FetchDataNode fetchDataNode, boolean full) {
        super(fetchDataNode, full);
        this.fetchableType = fetchDataNode.getFetchableType();
        this.fetchedResponseType = fetchDataNode.getFetchedResponseType();
        this.fetchServiceUrl = fetchDataNode.getFetchServiceUrl();
        this.headers = fetchDataNode.getHeaders();
        this.parameters = fetchDataNode.getParameters();
        this.validated = fetchDataNode.isValidated();
    }

    public static FetchDataNodeDto of(FetchDataNode node) {
        return node == null ? null : new FetchDataNodeDto(node,false);
    }

    public static FetchDataNodeDto off(FetchDataNode node) {
        return node == null ? null : new FetchDataNodeDto(node,true);
    }
}
