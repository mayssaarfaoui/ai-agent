package com.aimanager.agent.repositories;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.QuestionWithFreeFormNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface QuestionWithFreeFormNodeRepository extends GraphNodeRepository<QuestionWithFreeFormNode> {

    Optional<QuestionWithFreeFormNode> findByIdAndGraph(Long id, GraphEntity graph);

    Page<QuestionWithFreeFormNode> findByGraph(GraphEntity graph,Pageable pageable);

    boolean existsByGraphAndQuestionText(GraphEntity graph, String questionText);
}
