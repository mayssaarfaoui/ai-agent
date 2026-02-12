package com.aimanager.agent.prompts;

import com.aimanager.agent.enums.TypeBase;

public enum PromptAPIType implements TypeBase {
  MISTRAL_INSTRUCTION_PROMPT(Aliases.MISTRAL_INSTRUCTION_PROMPT, 1),
  OPEN_AI_PROMPT(Aliases.OPEN_AI_PROMPT, 2);

  private String name;
  private int id;

  PromptAPIType(String name, int id) {
    this.name = name;
    this.id = id;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  public static class Aliases {
    public static final String MISTRAL_INSTRUCTION_PROMPT = "Mistral Instruction Prompt";
    public static final String OPEN_AI_PROMPT = "Open AI Prompt";
  }
}
