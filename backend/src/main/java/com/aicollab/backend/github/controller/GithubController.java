package com.aicollab.backend.github.controller;

import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.github.dto.response.GithubRepoResponse;
import com.aicollab.backend.github.service.GithubService;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.infrastructure.github.GithubRestClient;
import com.aicollab.backend.infrastructure.github.dto.response.GithubFileContentResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;
    private final GithubRestClient githubRestClient;
    private final UserRepository userRepository;

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

    @GetMapping("/file")
    public ApiResponse<GithubFileContentResponse> getFileContent(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam String path,
            @RequestParam String sha
    ) {
        return ApiResponse.success(githubService.getFileContent(owner, repo, path, sha));
    }

    @GetMapping("/repos")
    public ApiResponse<List<GithubRepoResponse>> getRepos(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.getGithubAccessToken() == null) {
            throw new IllegalStateException("GitHub access token not found");
        }

        return ApiResponse.success(
                githubService.getRepos(user.getGithubAccessToken())
        );
    }

    // ========== PR Head SHA API ========== //
    @GetMapping("/prs/{prNumber}/head")
    public ApiResponse<String> getPrHeadSha(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String owner,
            @RequestParam String repo,
            @PathVariable int prNumber
    ) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        String sha = githubRestClient.getPrHeadSha(
                owner,
                repo,
                prNumber,
                user.getGithubAccessToken()
        );

        return ApiResponse.success(sha);
    }
}