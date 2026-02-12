package com.aimanager.agent.nodes;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.services.StoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Scope("prototype")
@Component
public class IteratorAgent extends WorkflowAgent {

    // Pointer to track the current position during iteration
    private int currentPosition = -1;

    protected List<Object> currentCollection = new ArrayList<>();

    @Autowired
    StoreDataService storeDataService;

    public IteratorAgent(){
        super(NodeType.IteratorNode);
    }

    public void loadData(Long nodeID){
        this.clear();
        this.currentCollection = storeDataService.getStoredData(nodeID);
    }

    public void deleteStoredData(Long nodeID){
        storeDataService.deleteStoredData(nodeID);
    }

    /**
     * Returns the next item in the collection and advances the pointer.
     *
     * @return The next item or null if no more items.
     */
    public Object getnext() {
        if (hasnext()) {
            return currentCollection.get(++currentPosition);
        }
        return null;
    }

    /**
     * Checks if more items are available in the collection.
     *
     * @return True if more items exist, false otherwise.
     */
    public boolean hasnext() {
        return currentCollection != null && currentPosition + 1 < currentCollection.size();
    }

    /**
     * Returns the highest pointer position.
     *
     * @return The highest pointer position or -1 if no data.
     */
    public int highwatermark() {
        return currentCollection != null ? currentCollection.size() - 1 : -1;
    }

    /**
     * Returns the lowest pointer position.
     *
     * @return The lowest pointer position, always 0 if data exists, otherwise -1.
     */
    public int lowwatermark() {
        return currentCollection != null && !currentCollection.isEmpty() ? 0 : -1;
    }

    /**
     * Returns the size of the current collection.
     *
     * @return The number of items in the current collection.
     */
    public int size() {
        return currentCollection != null ? currentCollection.size() : 0;
    }

    /**
     * Clears the hash table and resets the state.
     */
    public void clear() {
       // hashTable.clear();
        currentCollection.clear();
        currentPosition = -1;
    }

    /**
     * Error handling for empty collection or invalid pointer position.
     *
     * @param message The error message.
     */
    private void handleError(String message) {
        throw new IllegalStateException(message);
    }

    @Override
    public void execute(NodeContext context) {

    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable f) {
        return null;
    }
}
