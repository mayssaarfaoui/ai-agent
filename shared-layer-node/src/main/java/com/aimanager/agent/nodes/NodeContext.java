package com.aimanager.agent.nodes;

import java.util.HashMap;
import java.util.Map;

public class NodeContext {
    private Map<String, Object> data = new HashMap<>();

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object getData(String key) {
        if (data == null || !data.containsKey(key)) {
            return null;
        }
        return data.get(key);
    }

    @Override
    public String toString() {
        return "NodeContext{" +
                "data=" + data +
                '}';
    }
}
