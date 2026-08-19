package com.aimanager.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.aimanager.agent.dto.CommitDto;
import com.aimanager.agent.dto.FullCommitDto;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.services.GraphCommitServer;
import com.aimanager.agent.utils.PageParams;
import com.aimanager.agent.utils.RPage;
import com.aimanager.agent.utils.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/graph-commits")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Graph Commits", description = "Endpoints for managing graph commits")
public class GraphCommitController extends BaseController {

    @Autowired
    private GraphCommitServer graphCommitServer;

    // ==================== ENDPOINTS EXISTANTS ====================

    @Operation(summary = "Get list of commits", description = "Get list of commits for a given graph")
    @GetMapping
    public Response<RPage<CommitDto>> getCommits(
            @RequestParam(required = false) @Parameter(description = "Graph's ID") Long graphId,
            @ModelAttribute PageParams pageParams) {
        Page<Commit> commits = graphCommitServer.getCommits(graphId, pageParams.getPageable());
        RPage<CommitDto> rPage = RPage.of(commits, CommitDto::of);
        return Response.success(null, rPage);
    }

    @Operation(summary = "Get commit by ID", description = "Get commit by ID")
    @GetMapping("/get")
    public Response<FullCommitDto> getCommit(
            @RequestParam(required = true) @Parameter(description = "Graph's ID") Long graphId,
            @RequestParam(required = true) @Parameter(description = "Commit's ID") Long commitId) {
        Commit commit = graphCommitServer.getCommit(graphId, commitId);
        return Response.success(null, FullCommitDto.of(commit));
    }

    // ==================== NOUVEAUX ENDPOINTS ====================

    @Operation(
            summary = "Get all commits by graph ID",
            description = "Retrieves a list of all commits belonging to a specific graph (without pagination)"
    )
    @GetMapping("/graph/{graphId}")
    public Response<List<CommitDto>> getAllCommitsByGraphId(
            @Parameter(description = "The graph's ID", required = true, example = "123")
            @PathVariable Long graphId) {
        List<Commit> commits = graphCommitServer.getAllCommitsByGraphId(graphId);
        List<CommitDto> commitDtos = commits.stream()
                .map(CommitDto::of)
                .collect(Collectors.toList());
        return Response.success(null, commitDtos);
    }

    @Operation(
            summary = "Get commit by graph ID and commit ID",
            description = "Retrieves a specific commit using the graph ID and commit ID (path variables)"
    )
    @GetMapping("/graph/{graphId}/commit/{commitId}")
    public Response<FullCommitDto> getCommitByGraphAndCommitId(
            @Parameter(description = "The graph's ID", required = true, example = "123")
            @PathVariable Long graphId,
            @Parameter(description = "The commit's ID", required = true, example = "456")
            @PathVariable Long commitId) {
        Commit commit = graphCommitServer.getCommit(graphId, commitId);
        return Response.success(null, FullCommitDto.of(commit));
    }

    // ==================== ENDPOINTS OPTIONNELS ====================

    @Operation(
            summary = "Get latest commit by graph ID",
            description = "Retrieves the most recent commit for a specific graph"
    )
    @GetMapping("/graph/{graphId}/latest")
    public Response<FullCommitDto> getLatestCommitByGraphId(
            @Parameter(description = "The graph's ID", required = true, example = "123")
            @PathVariable Long graphId) {
        Commit commit = graphCommitServer.getLatestCommitByGraphId(graphId);
        return Response.success(null, FullCommitDto.of(commit));
    }

    @Operation(
            summary = "Get paginated commits by graph ID",
            description = "Retrieves a paginated list of commits belonging to a specific graph (alternative to the existing endpoint)"
    )
    @GetMapping("/graph/{graphId}/paged")
    public Response<RPage<CommitDto>> getCommitsByGraphIdPaged(
            @Parameter(description = "The graph's ID", required = true, example = "123")
            @PathVariable Long graphId,
            @Parameter(description = "Page number. By default, we get the first page.")
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Page size. By default, we get 10 records.")
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        PageParams pageParams = new PageParams();
        pageParams.setPageNumber(pageNumber);
        pageParams.setPageSize(pageSize);

        Page<Commit> commits = graphCommitServer.getCommits(graphId, pageParams.getPageable());
        RPage<CommitDto> rPage = RPage.of(commits, CommitDto::of);
        return Response.success(null, rPage);
    }
}