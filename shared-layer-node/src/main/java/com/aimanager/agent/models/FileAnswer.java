package com.aimanager.agent.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("FILE_ANSWER")
@Getter
@Setter
public class FileAnswer extends AnswerNode {

    private String answerFilePath;

    public FileAnswer() {
        super();
    }

    public FileAnswer(String answerFilePath) {
        super();
        this.answerFilePath = answerFilePath;
    }

    @Override
    public FileAnswer clone() {
        FileAnswer clone = new FileAnswer();
        super.copyData(clone);
        clone.setAnswerFilePath(this.answerFilePath);
        return clone;
    }

    @Override
    public String getLabel() {
        return super.getLabel() + "_" + answerFilePath;
    }

    @Override
    public String getAnswerText(){
        return answerFilePath;
    }

    @Override
    public String getAnswerKey(){
        int startIndex = answerFilePath.lastIndexOf("=");
        int endIndex = answerFilePath.lastIndexOf(".");
        return answerFilePath.substring(startIndex, endIndex);
    }

    @Override
    public String getAnswerValue(){
        return answerFilePath;
    }
    
}
