package com.aimanager.agent.models;

import com.aimanager.agent.enums.SendType;
import com.aimanager.agent.nodes.NodeContext;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@DiscriminatorValue("QUESTION_FREE_FORM")
public class QuestionWithFreeFormNode extends GraphNode {

    @Column(name = "f_f_question_text")
    private String questionText;

    @Column(name = "f_f_send_response")
    private boolean sendResponse;

    @Column(name = "f_f_send_type")
    @Enumerated(EnumType.STRING)
    private SendType sendType;

    @Column(name = "f_f_send_parameter")
    private String responseParameterName;

    @Column(name = "f_f_send_to")
    private String sendTo;

    @ElementCollection
    @CollectionTable(
            schema = "aimanager",
            name = "question_node_headers",
            joinColumns = @JoinColumn(name = "question_node_id")
    )
    @Column(name = "value")
    @MapKeyColumn(name = "key")
    private Map<String, String> headers;

    public QuestionWithFreeFormNode() {
        super(NodeType.F_F_QUESTION);
        this.headers = new HashMap<>();
    }

    public QuestionWithFreeFormNode(String question) {
        super(NodeType.F_F_QUESTION);
        this.questionText = question;
        this.headers = new HashMap<>();
    }

    @Override
    public QuestionWithFreeFormNode clone() {
        QuestionWithFreeFormNode clone = new QuestionWithFreeFormNode();
        super.copyData(clone);
        clone.setQuestionText(this.questionText);
        clone.setSendResponse(this.sendResponse);
        clone.setSendTo(this.sendTo);
        clone.setSendType(this.sendType);
        clone.setResponseParameterName(this.responseParameterName);
        clone.setHeaders(new HashMap<>(this.headers));
        return clone;
    }

    @Override
    public void setup() {
    }

    @Override
    public void process(NodeContext context) {
        logger.info("Free Form Question: " + questionText);
    }

    @Override
    public String getLabel() {
        return type.getName() + "_" + id + "_" + questionText;
    }


}
