package com.aicollab.backend.infrastructure.github;

import com.aicollab.backend.github.dto.response.GithubRepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubRestClient {

    @Value("${github.access-token}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public Object getCommit(String owner, String repo, String sha) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + sha;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Object.class
        );

        return response.getBody();
    }

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
}
