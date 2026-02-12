package com.aimanager.agent.models;

public enum IteratorType {
    ITERATOR_TASK(Aliases.ITERATOR_TASK),
    ITERATOR_USER(Aliases.ITERATOR_USER),
    ITERATOR_PR(Aliases.ITERATOR_PR);

    private final String type;

    IteratorType(String type) {
      this.type = type;
    }
  
    public String getType() {
      return type;
    }

  
    public String getName() {
      return getType();
    }
  
    public static class Aliases {
      public static final String ITERATOR_TASK = "ITERATOR_TASK";
      public static final String ITERATOR_USER = "ITERATOR_USER";
        public static final String ITERATOR_PR = "ITERATOR_PR";
    }
}


