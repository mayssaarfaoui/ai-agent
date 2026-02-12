package com.aimanager.agent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.GraphEntity;

@Repository
public interface GraphRepository extends JpaRepository<GraphEntity, Long> {

}
