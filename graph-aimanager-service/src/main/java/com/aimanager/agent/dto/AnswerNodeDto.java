package com.aimanager.agent.dto;


import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.DateAnswer;
import com.aimanager.agent.models.FileAnswer;
import com.aimanager.agent.models.NumberAnswer;
import com.aimanager.agent.models.TextAnswer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerNodeDto extends NodeDto {

    private final String answerText;

    public AnswerNodeDto(AnswerNode answerNode, boolean full) {
        super(answerNode, full);
        System.out.println("Processing AnswerNode node with id : "+answerNode.getId());
        switch(answerNode.getAnswerType()){
            case TEXT:
                this.answerText = ((TextAnswer) answerNode).getAnswerText();
                break;
            case NUMBER:
                this.answerText = ((NumberAnswer) answerNode).getAnswerNumber().toString();
                break;
            case FILE:
                this.answerText = ((FileAnswer) answerNode).getAnswerFilePath();
                break;
            case DATE:
                this.answerText = ((DateAnswer) answerNode).getAnswerDate().getTimeInMillis() + "";
                break;
            default:
                throw new IllegalArgumentException("Answer type not supported");
        }
    }

    public static AnswerNodeDto of(AnswerNode answerNode) {
   return answerNode == null ? null : new AnswerNodeDto(answerNode,false);
        
   }
   public static AnswerNodeDto off(AnswerNode answerNode) {
    return answerNode == null ? null : new AnswerNodeDto(answerNode,true);
         
    }
}
