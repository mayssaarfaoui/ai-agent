package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.FetchDataNode;
import com.aimanager.agent.models.GraphEntity;

@Repository
public interface FetchDataNodeRepository extends GraphNodeRepository<FetchDataNode> {

    Optional<FetchDataNode> findByIdAndGraph(Long id, GraphEntity graph);

}
