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

    public ResponseEntity<String> getPullRequest(String owner, String repo) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/pulls",
                owner, repo
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
    }
}
