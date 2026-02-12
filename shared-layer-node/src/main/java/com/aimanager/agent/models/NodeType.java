package com.aimanager.agent.models;

public enum NodeType {
    START(Aliases.START),
    END(Aliases.END),
    QUESTION(Aliases.QUESTION),
    ANSWER(Aliases.ANSWER),
    FETCH_DATA(Aliases.FETCH_DATA),
    ITERATOR(Aliases.ITERATOR),
    DOWNSTREAM(Aliases.DOWNSTREAM),
    STATEMENT(Aliases.STATEMENT),
    LLM(Aliases.LLM),
    SIMILARITY_SEARCH(Aliases.SIMILARITY_SEARCH),
    GOOGLE_TAXONOMY(Aliases.GOOGLE_TAXONOMY),
    SUBGRAPH(Aliases.SUBGRAPH),
    CREATE_CONVERSATION(Aliases.CREATE_CONVERSATION),
    F_F_QUESTION(Aliases.F_F_QUESTION),
    NOTIFICATION(Aliases.NOTIFICATION);
  
    private final String type;
  
    NodeType(String type) {
      this.type = type;
    }
  
    public String getType() {
      return type;
    }

  
    public String getName() {
      return getType();
    }
  
    public static class Aliases {
      public static final String START = "START";
      public static final String END = "END";
      public static final String QUESTION = "QUESTION";
      public static final String ANSWER = "ANSWER";
      public static final String FETCH_DATA = "FETCH_DATA";
      public static final String ITERATOR = "ITERATOR";
      public static final String DOWNSTREAM = "DOWNSTREAM";
      public static final String STATEMENT = "STATEMENT";
      public static final String LLM = "LLM";
      public static final String SIMILARITY_SEARCH = "SIMILARITY_SEARCH";
      public static final String GOOGLE_TAXONOMY = "GOOGLE_TAXONOMY";
      public static final String SUBGRAPH = "SUBGRAPH";
      public static final String CREATE_CONVERSATION = "CREATE_CONVERSATION";
      public static final String F_F_QUESTION = "F_F_QUESTION";
      public static final String NOTIFICATION = "NOTIFICATION";
    }
}


