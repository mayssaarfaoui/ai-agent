package com.aimanager.agent.repositories;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.FileAnswer;
import com.aimanager.agent.models.QuestionNode;

@Repository
public interface FileAnswerRepository extends AnswerNodeRepository<FileAnswer> {
    public boolean existsByQuestionAndAnswerFilePath(QuestionNode question, String answerFilePath);
}
