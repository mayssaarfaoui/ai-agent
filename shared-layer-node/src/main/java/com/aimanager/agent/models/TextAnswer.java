package com.aimanager.agent.models;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("TEXT_ANSWER")
@Getter
@Setter
public class TextAnswer extends AnswerNode {

    @Column(name = "answer_text")
    private String answer;

    public TextAnswer() {
        super();
    }

    public TextAnswer(String answerText) {
        super();
        this.answer = answerText;
    }

    @Override
    public TextAnswer clone() {
        TextAnswer clone = new TextAnswer();
        super.copyData(clone);
        clone.setAnswer(this.answer);
        return clone;
    }

    @Override
    public String getLabel() {
        return super.getLabel() + "_" + answer;
    }   

    @Override
    public String getAnswerText(){
        return answer;
    }

    @Override
    public String getAnswerKey(){
        return answer;
    }

    @Override
    public String getAnswerValue(){
        return answer;
    }
    
   

}
