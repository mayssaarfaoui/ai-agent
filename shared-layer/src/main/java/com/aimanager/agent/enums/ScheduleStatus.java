package com.aimanager.agent.enums;

import static java.lang.System.exit;

public enum ScheduleStatus implements TypeBase {
  BehindSchedule("Behind Schedule", 101),
  OnSchedule("On Track", 201),
  AheadOfSchedule("Ahead of schedule", 301);

  private final int id;
  private final String name;

  ScheduleStatus(String name, int id) {
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
    return AnswerType.SCHEDULE_STATUS;
  }

  public static boolean isValid(String type) {
    switch (type) {
      case "behind":
      case "on track":
      case "ahead":
        return true;
      default:
        System.out.println("Invalid Schedule Status Type");
        exit(1);
    }
    return false;
  }

}