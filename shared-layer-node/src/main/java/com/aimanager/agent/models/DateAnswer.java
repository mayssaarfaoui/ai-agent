package com.aimanager.agent.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DiscriminatorValue("DATE_ANSWER")
@Getter
@Setter
public class DateAnswer extends AnswerNode {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @Column(name="answer_date")
    private Calendar answerDate;

    public DateAnswer() {   
        super();
    }

    public DateAnswer(Calendar answerDate) {
        super();
        this.answerDate = answerDate;
    }

    @Override
    public DateAnswer clone() {
        DateAnswer clone = new DateAnswer();
        super.copyData(clone);
        clone.setAnswerDate(this.answerDate);
        return clone;
    }

    @Override
    public String getLabel() {
        return super.getLabel() + "_" + String.valueOf(answerDate.getTimeInMillis());
    }

    @Override
    public String getAnswerText(){
        return String.valueOf(answerDate.getTimeInMillis());
    }

    @Override
    public String getAnswerKey(){
        return String.valueOf(answerDate.getTimeInMillis());
    }

    @Override
    public String getAnswerValue(){
        return String.valueOf(answerDate.getTimeInMillis());
    }
    
    
    
}
