package com.aimanager.agent.services;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.PrGitHUB;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenPRService implements DownStreamProcess {

    private  boolean isDone;
    private String message;
    private Map<String,String> options;

    public OpenPRService(){
        super();
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public void process(Object data) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            PrGitHUB item =  mapper.readValue(data.toString(), PrGitHUB.class);
            this.message="Click on the Following link to view the PR :";
            options = new HashMap<>();
            options.put("PR_LINK",item.getPrUrl());
            this.isDone = true;
        }catch (Exception e){
            throw new IllegalArgumentException("Data can to be converted to task");
        }
    }
}
