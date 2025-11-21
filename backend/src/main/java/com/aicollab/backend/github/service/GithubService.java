package com.aicollab.backend.github.service;

import com.aicollab.backend.infrastructure.github.GitHubClient;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GitHubClient gitHubClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PullRequestResponse> getPullRequests(String owner, String repo) {
        var response = gitHubClient.getPullRequest(owner, repo);

        try {
            return objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<PullRequestResponse>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pull requests from " + owner + " " + repo);
        }
    }
}
