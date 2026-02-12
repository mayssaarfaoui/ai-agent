package com.aimanager.agent.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aimanager.agent.models.GoogleTaxonomyNode;
import com.aimanager.agent.models.GraphEntity;

@Repository
public interface GoogleTaxonomyNodeRepository extends JpaRepository<GoogleTaxonomyNode, Long> {
    Page<GoogleTaxonomyNode> findByGraph(GraphEntity graph, Pageable pageable);
    Optional<GoogleTaxonomyNode> findByGraphAndId(GraphEntity graph, Long id);

}
