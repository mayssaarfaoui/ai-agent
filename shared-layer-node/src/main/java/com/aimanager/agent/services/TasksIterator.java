package com.aimanager.agent.services;
/*
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.Task;
import com.aimanager.agent.nodes.IteratorAgent;
import com.aimanager.agent.nodes.NodeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TasksIterator<T extends Task> extends IteratorAgent {

    @Autowired
    StoreDataService storeDataService;

    public TasksIterator(){
        super();
    }

    @Override
    public void loadData(Long nodeID) {
        this.clear();
        this.currentCollection = storeDataService.getStoredData(nodeID);
    }

    @Override
    public void deleteStoredData(Long nodeID) {
        storeDataService.deleteStoredData(nodeID);
    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable f) {
        return null;
    }
}*/
