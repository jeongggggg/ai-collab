package com.aicollab.backend.github.service;

import com.aicollab.backend.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.GitHubClient;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GitHubClient gitHubClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<PullRequestResponse> getPullRequests(String owner, String repo) {
        var response = gitHubClient.getPullRequest(owner, repo);

        try {
            return objectMapper.readValue(
                    response.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, PullRequestResponse.class
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pull requests from " + owner + " " + repo);
        }
    }

    public List<PullRequestFileResponse> getPullRequestFiles(String owner, String repo, int prNumber) {
        var response = gitHubClient.getPullRequestFiles(owner, repo, prNumber);

        try {
            return objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<PullRequestFileResponse>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse PR file list: " + e.getMessage());
        }
    }
}
