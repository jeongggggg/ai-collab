package com.aicollab.backend.github.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    @Value("${github.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/vnd.github+json");
        return headers;
    }

    // PR 목록
    public ResponseEntity<String> getPullRequest(String owner, String repo) {
        String url = "https://api.github.com/repos/%s/%s/pulls".formatted(owner, repo);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers()), String.class);
    }

    // PR 파일 목록
    public ResponseEntity<String> getPullRequestFiles(String owner, String repo, int prNumber) {
        String url = "https://api.github.com/repos/%s/%s/pulls/%d/files".formatted(owner, repo, prNumber);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers()), String.class);
    }

    // 파일 원본 조회
    public ResponseEntity<String> getFileContent(String owner, String repo, String path, String sha) {
        String url = "https://api.github.com/repos/%s/%s/contents/%s?ref=%s"
                .formatted(owner, repo, path, sha);

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers()), String.class);
    }

    // commit → PR 자동 조회
    public ResponseEntity<String> getPrByCommit(String owner, String repo, String sha) {
        String url = "https://api.github.com/repos/%s/%s/commits/%s/pulls"
                .formatted(owner, repo, sha);

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers()), String.class);
    }
}