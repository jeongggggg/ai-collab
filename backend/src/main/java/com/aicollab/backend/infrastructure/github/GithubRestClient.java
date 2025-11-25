package com.aicollab.backend.infrastructure.github;

import com.aicollab.backend.github.dto.response.GithubRepoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubRestClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // ========== 기존 commit 조회 ========== //
    public Object getCommit(String owner, String repo, String sha, String accessToken) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + sha;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Object.class
        );

        return response.getBody();
    }

    // ========== Repo 목록 조회 ========== //
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

        return List.of(response.getBody());
    }

    // ========== PR Head SHA 조회 ========== //
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

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("head").path("sha").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract head SHA");
        }
    }
}