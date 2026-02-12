package com.aimanager.agent.models;

public enum FetchableType {
    FETCH_TASK(Aliases.FETCH_TASK),
    FETCH_USER(Aliases.FETCH_USER),
    FETCH_PR(Aliases.FETCH_PR);

    private final String type;

    FetchableType(String type) {
      this.type = type;
    }
  
    public String getType() {
      return type;
    }

  
    public String getName() {
      return getType();
    }
  
    public static class Aliases {
      public static final String FETCH_TASK = "FETCH_TASK";
      public static final String FETCH_USER = "FETCH_USER";
        public static final String FETCH_PR = "FETCH_PR";
    }
}


