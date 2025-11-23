package com.aicollab.backend.infrastructure.github;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    @Value("${github.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/vnd.github+json");
        return headers;
    }

    // PR 목록 조회
    public ResponseEntity<String> getPullRequest(String owner, String repo) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls", owner, repo);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(defaultHeaders()), String.class);
    }

    // PR에 포함된 파일 조회
    public ResponseEntity<String> getPullRequestFiles(String owner, String repo, int prNumber) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files", owner, repo, prNumber);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(defaultHeaders()), String.class);
    }

    // 파일 내용 조회
    public ResponseEntity<String> getFileContent(String owner, String repo, String path, String ref) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/contents/%s?ref=%s",
                owner, repo, path, ref
        );
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(defaultHeaders()), String.class);
    }

    // commit → PR 자동 조회 API
    public ResponseEntity<String> getPrByCommit(String owner, String repo, String sha) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/commits/%s/pulls",
                owner, repo, sha
        );
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(defaultHeaders()), String.class);
    }
}
