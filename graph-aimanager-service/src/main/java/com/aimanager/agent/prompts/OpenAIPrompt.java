package com.aimanager.agent.prompts;

public class OpenAIPrompt extends Prompt {
  private String prompt;

  public OpenAIPrompt(String prompt) {
    super(PromptAPIType.OPEN_AI_PROMPT);
    this.prompt = prompt;
  }

  public String getPrompt() {
    return prompt;
  }
}
