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

@RestController
@RequestMapping("/graph-commits")
@CrossOrigin("http://localhost:3000")
public class GraphCommitController extends BaseController {

    @Autowired
    private GraphCommitServer graphCommitServer;

    @Operation(summary = "Get list of commits", description = "Get list of commits for a given graph")
    @RequestMapping(method = RequestMethod.GET)
    public Response<RPage<CommitDto>> getCommits(
        @RequestParam(required = false) @Parameter(description = "Graph's ID") Long graphId,
        @ModelAttribute PageParams pageParams) {
            Page<Commit> commits = graphCommitServer.getCommits(graphId, pageParams.getPageable());
            RPage<CommitDto> rPage = RPage.of(commits, CommitDto::of);
            return Response.success(null,rPage);
    }


    @Operation(summary = "Get commit by ID", description = "Get commit by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public Response<FullCommitDto> getCommit(
        @RequestParam(required = true) @Parameter(description = "Graph's ID") Long graphId,
        @RequestParam(required = true) @Parameter(description = "Commit's ID") Long commitId) {
            Commit commit = graphCommitServer.getCommit(graphId, commitId);
            return Response.success(null,FullCommitDto.of(commit));
    }

}
