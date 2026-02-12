package com.aimanager.agent.enums;

public enum TaskStatus implements TypeBase {
  NOT_STARTED("Not Started", 1),
  IN_PROGRESS("In Progress", 2),
  BLOCKED("Blocked", 3),
  CANCELLED("Cancelled", 4),
  COMPLETED("Completed", 5);

    private final int id;
    private final String name;

    TaskStatus(String name, int id) {
        this.id = id;
        this.name = name;
    }

  @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public AnswerType getAnswerType() {
        return AnswerType.TASK_STATUS;
    }

  public static boolean isValid(String key) {
        switch (key.toLowerCase()) {
            case "not started":
          case "in progress":
          case "blocked":
          case "cancelled":
          case "completed":
            return true;
          default:
            throw new IllegalArgumentException("Invalid Task Status Type : " + key);
        }
    }
}
