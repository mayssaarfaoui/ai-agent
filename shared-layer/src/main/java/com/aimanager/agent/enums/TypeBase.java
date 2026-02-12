package com.aimanager.agent.enums;/* see LICENSE file in the root */

public interface TypeBase {
  static boolean isTypeBase(TypeBase key) {
    return key != null;
  }

  public int getId(); // Fixme use IDGenerator.generateID() String instead of int
  public String getName();

}