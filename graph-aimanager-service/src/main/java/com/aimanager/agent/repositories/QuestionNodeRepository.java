package com.aimanager.agent.repositories;

import com.aimanager.agent.models.QuestionNode;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.GraphEntity;


@Repository
public interface QuestionNodeRepository extends GraphNodeRepository<QuestionNode> {

    Optional<QuestionNode> findByIdAndGraph(Long id, GraphEntity graph);

    Page<QuestionNode> findByGraph(GraphEntity graph,Pageable pageable);

    boolean existsByGraphAndQuestionText(GraphEntity graph, String questionText);
}
