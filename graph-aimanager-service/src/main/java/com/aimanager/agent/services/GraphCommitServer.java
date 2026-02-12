package com.aimanager.agent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aimanager.agent.dto.CommitDto;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.CommitRepository;
import com.aimanager.agent.repositories.GraphRepository;

@Service
public class GraphCommitServer {

    @Autowired
    CommitRepository commitRepository;

    @Autowired
    GraphRepository graphRepository;

    /*
     * get graph by id
     */

    public GraphEntity getGraph(Long graphId) {
        return graphRepository.findById(graphId).orElseThrow(() -> new RuntimeException("Graph not found with id: " + graphId));
    }

    /*
     * get commits for a graph
     */

    public Page<Commit> getCommits(Long graphId, Pageable pageable) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.findByGraphOrderByCreatedAtDesc(graph, pageable);
    }

    /*
     * get commit by id
     */

    public Commit getCommit(Long graphId, Long commitId) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.findByIdAndGraph(commitId, graph).orElseThrow(
            () -> new RuntimeException("Commit not found with id: " + commitId + " for graph: " + graphId));
    }

}
