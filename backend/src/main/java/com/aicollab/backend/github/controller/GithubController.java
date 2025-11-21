package com.aicollab.backend.github.controller;

import com.aicollab.backend.github.service.GithubService;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
