package com.aimanager.agent.utils;

import com.aimanager.agent.enums.TypeBase;

public enum YesNoIDK implements TypeBase {
  YES("yes", 1),
  NO("no", 2),
  I_DONT_KNOW("I don't know", 3),
  Y("y", 4),
  N("n", 5),
  I("idk", 6);

  private final String value;
  private final int id;

  YesNoIDK(String value, int id) {
    this.value = value;
    this.id = id;
  }

  @Override
  public int getId() {
    return 0;
  }

  @Override
  public String getName() {
    return "";
  }
}
