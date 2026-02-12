package com.aimanager.agent.repositories;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.models.commits.CommitStatus;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {

    public int deleteByGraph(GraphEntity graph);

    Page<Commit> findByGraphOrderByCreatedAtDesc(GraphEntity graph, Pageable pageable);

    Optional<Commit> findByIdAndGraph(Long id, GraphEntity graph);

    @Query("SELECT c FROM Commit c WHERE c.graph = :graph ORDER BY c.createdAt ASC")
    List<Commit> findFirstByGraphOrderByCreatedAtAsc(GraphEntity graph);

    public default Commit getFirstGraphCommit(GraphEntity graph){
        List<Commit> commits = findFirstByGraphOrderByCreatedAtAsc(graph);
        if(commits == null || commits.isEmpty()){
            return null;
        }
        return commits.get(0);
    }

    @Query("SELECT c FROM Commit c WHERE c.graph = :graph ORDER BY c.createdAt DESC")
    List<Commit> findLastByGraphOrderByCreatedAtDesc(GraphEntity graph);

    public default Commit getLatestGraphCommit(GraphEntity graph){
        List<Commit> commits = findLastByGraphOrderByCreatedAtDesc(graph);
        if(commits == null || commits.isEmpty()){
            return null;
        }
        return commits.get(0);
    }

    boolean existsByGraphAndStatus(GraphEntity graph, CommitStatus status);

    List<Commit> findByGraphAndStatus(GraphEntity graph, CommitStatus status);

    default Commit getCurrentGraphCommit(GraphEntity graph){
        List<Commit> commits = findByGraphAndStatus(graph, CommitStatus.CREATED);
        if(commits == null || commits.isEmpty()){
            return null;
        }
        if(commits.size() > 1){
            throw new IllegalArgumentException("More than one commit found for graph " + graph.getId());
        }
        return commits.get(0);
    }

}
