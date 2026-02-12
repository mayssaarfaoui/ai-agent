package com.aimanager.agent.models;

import java.util.HashMap;
import java.util.Map;

public class SharedGraphData {

    public static Map<String, Object> nodes = new HashMap<>();

    public static void storeData(String key, Object value){
        nodes.put(key, value);
    }

    public static Object getData(String key){
        return nodes.get(key);
    }
}