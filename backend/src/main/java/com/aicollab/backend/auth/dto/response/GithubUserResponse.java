package com.aicollab.backend.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubUserResponse {

    private Long id;
    private String login;
    private String name;
    private String email;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    protected GithubUserResponse() {}

    public Long getId() {
        return id;
    }

    public String getLogin(){
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
