package com.aimanager.agent.models;

public enum DownStreamType {
    OPEN_PR(Aliases.OPEN_PR),
    SUMMARY_PR(Aliases.SUMMARY_PR),
    DECLINE_PR(Aliases.DECLINE_PR),
    REMINDER_PR(Aliases.REMINDER_PR);

    private final String type;

    DownStreamType(String type) {
      this.type = type;
    }
  
    public String getType() {
      return type;
    }

  
    public String getName() {
      return getType();
    }
  
    public static class Aliases {
      public static final String OPEN_PR = "OPEN_PR";
      public static final String SUMMARY_PR = "SUMMARY_PR";
        public static final String DECLINE_PR = "DECLINE_PR";
        public static final String REMINDER_PR = "REMINDER_PR";
    }
}


