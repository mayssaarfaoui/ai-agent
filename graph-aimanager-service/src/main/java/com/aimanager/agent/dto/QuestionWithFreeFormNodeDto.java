package com.aimanager.agent.dto;

import com.aimanager.agent.enums.SendType;
import com.aimanager.agent.models.QuestionWithFreeFormNode;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class QuestionWithFreeFormNodeDto extends NodeDto  {
    private final String question;
    private final boolean sendResponse;
    private final SendType sendType;
    private final String responseParameterName;
    private final String sendTo;
    private Map<String, String> headers;

    public QuestionWithFreeFormNodeDto(QuestionWithFreeFormNode questionNode,  boolean full){
        super(questionNode,full);
        this.question = questionNode.getQuestionText();
        this.sendResponse = questionNode.isSendResponse();
        this.sendType = questionNode.getSendType();
        this.responseParameterName = questionNode.getResponseParameterName();
        this.sendTo = questionNode.getSendTo();
        this.headers = questionNode.getHeaders();
    }
    public static QuestionWithFreeFormNodeDto of(QuestionWithFreeFormNode question) {
        return question == null ? null : new QuestionWithFreeFormNodeDto(question,false);
    }

    public static QuestionWithFreeFormNodeDto off(QuestionWithFreeFormNode question) {
        return question == null ? null : new QuestionWithFreeFormNodeDto(question,true);
    }
}
