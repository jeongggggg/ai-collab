package com.aicollab.backend.auth.service;

import com.aicollab.backend.auth.dto.response.GithubUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOAuthClient {
    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    // Github Access Token 요청
    public String requestAccessToken(String code){
        String url = "https://github.com/login/oauth/access_token";

        Map<String, String> body = Map.of(
                "client_id", clientId,

                "client_secret", clientSecret,
                "code", code
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );

        return (String) response.getBody().get("access_token");

    }

    // Github 유저 정보 요청
    public GithubUserResponse requestUserInfo(String accessToken){
        String url = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubUserResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GithubUserResponse.class
        );

        return response.getBody();
    }
}
