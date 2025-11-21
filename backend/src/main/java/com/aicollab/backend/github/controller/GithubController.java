package com.aicollab.backend.github.controller;

import com.aicollab.backend.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.github.service.GithubService;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/prs")
    public ApiResponse<List<PullRequestResponse>> getPullRequests(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return ApiResponse.success(githubService.getPullRequests(owner, repo));
    }

    @GetMapping("/prs/{prNumber}/files")
    public ApiResponse<List<PullRequestFileResponse>> getPullRequestFiles(
            @RequestParam String owner,
            @RequestParam String repo,
            @PathVariable int prNumber
    ) {
        return ApiResponse.success(
                githubService.getPullRequestFiles(owner, repo, prNumber)
        );
    }

}
