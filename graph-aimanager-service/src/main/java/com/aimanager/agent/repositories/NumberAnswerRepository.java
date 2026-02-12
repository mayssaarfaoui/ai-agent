package com.aimanager.agent.repositories;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.NumberAnswer;
import com.aimanager.agent.models.QuestionNode;

@Repository
public interface NumberAnswerRepository extends AnswerNodeRepository<NumberAnswer> {
    public boolean existsByQuestionAndAnswerNumber(QuestionNode question, Double answerNumber);
}
