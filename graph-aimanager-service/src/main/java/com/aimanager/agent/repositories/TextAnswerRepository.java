package com.aimanager.agent.repositories;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.QuestionNode;
import com.aimanager.agent.models.TextAnswer;

@Repository
public interface TextAnswerRepository extends AnswerNodeRepository<TextAnswer> {
    public boolean existsByQuestionAndAnswer(QuestionNode question, String answerText);

}
