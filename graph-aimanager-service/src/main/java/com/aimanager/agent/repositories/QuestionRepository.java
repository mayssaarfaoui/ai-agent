package com.aimanager.agent.repositories;


import com.aimanager.agent.models.QuestionNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository<T extends QuestionNode> extends JpaRepository<T, Long> {
}
