package com.aimanager.agent.prompts;

/*
 <s>[INST] Instruction [/INST] Model answer</s>[INST] Follow-up instruction [/INST]
 */

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.aimanager.agent.models.BaseEntity;

@Entity
@Table(schema = "aimanager", name = "prompt")
public abstract class Prompt extends BaseEntity {
  
  @Enumerated(EnumType.STRING)
  private PromptAPIType promptAPIType;

  Prompt(PromptAPIType promptAPIType) {
    this.promptAPIType = promptAPIType;
  }

  public PromptAPIType getPromptAPIType() {
    return promptAPIType;
  }
}
