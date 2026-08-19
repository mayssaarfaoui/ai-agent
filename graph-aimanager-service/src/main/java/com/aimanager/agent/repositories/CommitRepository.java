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

    // ✅ EXISTANT - Supprimer les commits d'un graphe
    public int deleteByGraph(GraphEntity graph);

    // ✅ EXISTANT - Commits paginés d'un graphe
    Page<Commit> findByGraphOrderByCreatedAtDesc(GraphEntity graph, Pageable pageable);

    // ✅ EXISTANT - Trouver un commit par ID et graphe
    Optional<Commit> findByIdAndGraph(Long id, GraphEntity graph);

    // ✅ EXISTANT - Premier commit d'un graphe (ordre ascendant)
    @Query("SELECT c FROM Commit c WHERE c.graph = :graph ORDER BY c.createdAt ASC")
    List<Commit> findFirstByGraphOrderByCreatedAtAsc(GraphEntity graph);

    // ✅ EXISTANT - Méthode par défaut pour obtenir le premier commit
    public default Commit getFirstGraphCommit(GraphEntity graph){
        List<Commit> commits = findFirstByGraphOrderByCreatedAtAsc(graph);
        if(commits == null || commits.isEmpty()){
            return null;
        }
        return commits.get(0);
    }

    // ✅ EXISTANT - Dernier commit d'un graphe (ordre descendant)
    @Query("SELECT c FROM Commit c WHERE c.graph = :graph ORDER BY c.createdAt DESC")
    List<Commit> findLastByGraphOrderByCreatedAtDesc(GraphEntity graph);

    // ✅ EXISTANT - Méthode par défaut pour obtenir le dernier commit
    public default Commit getLatestGraphCommit(GraphEntity graph){
        List<Commit> commits = findLastByGraphOrderByCreatedAtDesc(graph);
        if(commits == null || commits.isEmpty()){
            return null;
        }
        return commits.get(0);
    }

    // ✅ EXISTANT - Vérifier l'existence par statut
    boolean existsByGraphAndStatus(GraphEntity graph, CommitStatus status);

    // ✅ EXISTANT - Trouver les commits par statut
    List<Commit> findByGraphAndStatus(GraphEntity graph, CommitStatus status);

    // ✅ EXISTANT - Obtenir le commit courant (status CREATED)
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

    // ==================== NOUVELLES MÉTHODES ====================

    /**
     * ➕ NOUVEAU - Récupère tous les commits d'un graphe sans pagination
     * Utilisé par getAllCommitsByGraphId() dans le service
     */
    List<Commit> findByGraphOrderByCreatedAtDesc(GraphEntity graph);


    /**
     * ➕ NOUVEAU - Vérifie si un commit existe pour un graphe
     * Utile pour les validations sans charger l'entité
     */
    boolean existsByGraphIdAndId(Long graphId, Long commitId);

    /**
     * ➕ NOUVEAU - Compte le nombre de commits pour un graphe
     */
    long countByGraph(GraphEntity graph);

    /**
     * ➕ NOUVEAU - Trouve les commits entre deux dates
     */
    List<Commit> findByGraphAndCreatedAtBetweenOrderByCreatedAtDesc(
            GraphEntity graph,
            java.util.Date startDate,
            java.util.Date endDate
    );
}