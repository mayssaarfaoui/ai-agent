package com.aimanager.agent.repositories;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.DateAnswer;
import com.aimanager.agent.models.QuestionNode;

@Repository
public interface DateAnswerRepository extends AnswerNodeRepository<DateAnswer> {
    public boolean existsByQuestionAndAnswerDate(QuestionNode question, Calendar answerDate);
}
