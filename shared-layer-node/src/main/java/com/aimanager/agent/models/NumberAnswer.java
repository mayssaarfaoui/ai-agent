package com.aimanager.agent.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("NUMBER_ANSWER")
@Getter
@Setter
public class NumberAnswer extends AnswerNode {

    private Double answerNumber;

    public NumberAnswer() {
        super();
    }

    public NumberAnswer(Double answerNumber) {
        super();
        this.answerNumber = answerNumber;
    }

    @Override
    public NumberAnswer clone() {
        NumberAnswer clone = new NumberAnswer();
        super.copyData(clone);
        clone.setAnswerNumber(this.answerNumber);
        return clone;
    }

    @Override
    public String getLabel() {
        return super.getLabel() + "_" + answerNumber;
    }

    @Override
    public String getAnswerText(){
        return answerNumber.toString();
    }

    @Override
    public String getAnswerKey(){
        return answerNumber.toString();
    }

    @Override
    public String getAnswerValue(){
        return answerNumber.toString();
    }

}
