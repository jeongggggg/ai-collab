package com.aicollab.backend.infrastructure.github.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestResponse {

    private int number;
    private String title;
    private String state;

    @JsonProperty("created_at")
    private String createdAt;

    private GithubUser user;

    private Head head;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GithubUser {
        private String login;

        @JsonProperty("avatar_url")
        private String avatarUrl;

        private Long id;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Head {
        private String sha;
        private String ref;
    }
}