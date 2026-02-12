package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.QuestionNode;

@Repository
public interface AnswerNodeRepository<A extends AnswerNode> extends JpaRepository<A, Long> {
    Page<A> findByQuestion(QuestionNode question, Pageable pageable);
    Optional<A> findByIdAndQuestion(Long id, QuestionNode question);
    Optional<A> findByIdAndGraph(Long id, GraphEntity graph);
    int countByQuestion(QuestionNode question);
}
