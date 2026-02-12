package com.aimanager.agent.prompts;

import java.util.Map;
import java.util.HashMap;

/*
  A structured data record that should be added to the prompt
  Make child classes that import data from different sources
  and add the data to the map
 */
public class PromptStructuredData {
  private Map<String, String> map = new HashMap<>();

  public Map<String, String> getMap() {
    return map;
  }

  public void add(String key, String value) {
    map.put(key, value);
  }

  public void remove(String key) {
    map.remove(key);
  }

  public String get(String key) {
    return map.get(key);
  }

  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  public boolean containsValue(String value) {
    return map.containsValue(value);
  }

  public void setMap(Map<String, String> map) {
    this.map = map;
  }
}
