package com.aimanager.agent.dto;

import com.aimanager.agent.models.QuestionNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionNodeDto extends NodeDto {
    private final String questionType;
    private final String responseType;
    private final String question;

    public QuestionNodeDto(QuestionNode questionNode, boolean full) {
        super(questionNode, full);
        this.questionType = questionNode.getQuestionType().toString();
        this.responseType = questionNode.getAnswerType().toString();
        this.question = questionNode.getQuestionText();
    }

    public static QuestionNodeDto of(QuestionNode question) {
        return question == null ? null : new QuestionNodeDto(question,false);
    }

    public static QuestionNodeDto off(QuestionNode question) {
        return question == null ? null : new QuestionNodeDto(question,true);
    }
}
