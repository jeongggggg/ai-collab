package com.aicollab.backend.github.service;

import com.aicollab.backend.infrastructure.github.GitHubClient;
import com.aicollab.backend.infrastructure.github.dto.response.GithubFileContentResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestResponse;
import com.aicollab.backend.infrastructure.github.parser.DiffChange;
import com.aicollab.backend.infrastructure.github.parser.DiffParser;
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
    private final DiffParser diffParser;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // PR 목록 조회
    public List<PullRequestResponse> getPullRequests(String owner, String repo) {
        var response = gitHubClient.getPullRequest(owner, repo);
        try {
            return objectMapper.readValue(response.getBody(),
                    new TypeReference<List<PullRequestResponse>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to get pull requests");
        }
    }

    // PR 파일 목록 조회
    public List<PullRequestFileResponse> getPullRequestFiles(String owner, String repo, int prNumber) {
        var response = gitHubClient.getPullRequestFiles(owner, repo, prNumber);
        try {
            return objectMapper.readValue(response.getBody(),
                    new TypeReference<List<PullRequestFileResponse>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse PR file list");
        }
    }

    // 파일 전체 조회
    public GithubFileContentResponse getFileContent(String owner, String repo, String path, String sha) {
        var response = gitHubClient.getFileContent(owner, repo, path, sha);
        try {
            return objectMapper.readValue(response.getBody(), GithubFileContentResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file content");
        }
    }

    // Diff 파싱
    public List<DiffChange> parseDiffLines(String patch) {
        return diffParser.parse(patch);
    }

    // commit → 포함된 PR 번호 자동 조회
    public Integer getPrNumberByCommit(String owner, String repo, String sha) {
        var response = gitHubClient.getPrByCommit(owner, repo, sha);
        try {
            List<PullRequestResponse> result = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<PullRequestResponse>>() {}
            );
            if (result.isEmpty()) return null;
            return result.get(0).getNumber();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find PR for commit");
        }
    }
}