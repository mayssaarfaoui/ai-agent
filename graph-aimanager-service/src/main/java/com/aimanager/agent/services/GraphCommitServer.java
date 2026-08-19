package com.aimanager.agent.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aimanager.agent.dto.CommitDto;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.CommitRepository;
import com.aimanager.agent.repositories.GraphRepository;

import java.util.List;

@Service
public class GraphCommitServer {

    private static final Logger logger = LoggerFactory.getLogger(GraphCommitServer.class);

    @Autowired
    CommitRepository commitRepository;

    @Autowired
    GraphRepository graphRepository;

    /*
     * get graph by id
     */
    public GraphEntity getGraph(Long graphId) {
        return graphRepository.findById(graphId)
                .orElseThrow(() -> new RuntimeException("Graph not found with id: " + graphId));
    }

    /*
     * get commits for a graph (paginated) - EXISTANT
     */
    public Page<Commit> getCommits(Long graphId, Pageable pageable) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.findByGraphOrderByCreatedAtDesc(graph, pageable);
    }

    /*
     * get commit by id - EXISTANT
     */
    public Commit getCommit(Long graphId, Long commitId) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.findByIdAndGraph(commitId, graph)
                .orElseThrow(() -> new RuntimeException("Commit not found with id: " + commitId + " for graph: " + graphId));
    }

    // ==================== NOUVELLES MÉTHODES ====================

    /*
     * get all commits for a graph (without pagination) - NOUVEAU
     */
    public List<Commit> getAllCommitsByGraphId(Long graphId) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.findByGraphOrderByCreatedAtDesc(graph);
    }

    /*
     * get latest commit for a graph - CORRIGÉ
     * Utilise la méthode utilitaire existante au lieu de findFirstByGraphOrderByCreatedAtDesc
     */
    public Commit getLatestCommitByGraphId(Long graphId) {
        GraphEntity graph = getGraph(graphId);
        Commit commit = commitRepository.getLatestGraphCommit(graph);  // ← Utilise la méthode existante
        if (commit == null) {
            logger.warn("No commits found for graph: {}", graphId);
            throw new RuntimeException("No commits found for graph: " + graphId);
        }
        return commit;
    }

    /*
     * get first commit for a graph - NOUVEAU (optionnel)
     */
    public Commit getFirstCommitByGraphId(Long graphId) {
        GraphEntity graph = getGraph(graphId);
        Commit commit = commitRepository.getFirstGraphCommit(graph);  // ← Utilise la méthode existante
        if (commit == null) {
            logger.warn("No commits found for graph: {}", graphId);
            throw new RuntimeException("No commits found for graph: " + graphId);
        }
        return commit;
    }

    /*
     * check if commit exists for graph - NOUVEAU
     */
    public boolean commitExistsForGraph(Long graphId, Long commitId) {
        // Vérifie d'abord que le graphe existe
        if (!graphRepository.existsById(graphId)) {
            throw new RuntimeException("Graph not found with id: " + graphId);
        }
        return commitRepository.existsByGraphIdAndId(graphId, commitId);
    }

    /*
     * count commits for graph - NOUVEAU (optionnel)
     */
    public long countCommitsByGraphId(Long graphId) {
        GraphEntity graph = getGraph(graphId);
        return commitRepository.countByGraph(graph);
    }
}