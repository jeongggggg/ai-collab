package com.aicollab.backend.github.client;

import com.aicollab.backend.github.dto.response.GithubRepoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Commit 조회 (Raw JSON 반환)
    public void getCommit(String owner, String repo, String sha, String accessToken) {

        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + sha;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("GitHub API returned empty commit response");
        }
    }

    // Repo 목록 조회 (null-safe 처리)
    public List<GithubRepoResponse> getRepos(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubRepoResponse[]> response = restTemplate.exchange(
                "https://api.github.com/user/repos",
                HttpMethod.GET,
                entity,
                GithubRepoResponse[].class
        );

        GithubRepoResponse[] body = response.getBody();

        if (body == null) {
            log.warn("GitHub API returned null for /user/repos — returning empty list.");
            return Collections.emptyList();
        }

        return List.of(body);
    }

    // PR Head SHA 조회 (null-safe 처리)
    public String getPrHeadSha(String owner, String repo, int prNumber, String accessToken) {

        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + prNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        String body = response.getBody();
        if (body == null) {
            throw new RuntimeException("GitHub API returned empty PR response");
        }

        try {
            JsonNode root = mapper.readTree(body);
            return root.path("head").path("sha").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract PR head SHA", e);
        }
    }
}